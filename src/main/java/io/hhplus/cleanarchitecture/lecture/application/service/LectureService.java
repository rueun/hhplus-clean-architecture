package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureEnrollmentInfo;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureWithItems;
import io.hhplus.cleanarchitecture.lecture.application.dto.command.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureAlreadyEnrolledException;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;

    private final TimeProvider timeProvider;

    /**
     * 특강 신청
     * @param command 특강 신청 Command 객체
     * @return 신청된 LectureEnrollment 객체
     * @throws LectureAlreadyEnrolledException 유저가 동일한 강의에 이미 수강신청한 경우
     */
    @Transactional
    public LectureEnrollment enroll(final EnrollLectureCommand command) {
        LocalDateTime enrolledAt = timeProvider.now();

        final LectureItem lectureItem = lectureRepository.getItemByIdWithPessimisticLock(command.getLectureId(), command.getLectureItemId());
        lectureItem.enroll(enrolledAt);

        final LectureEnrollment enrollment = LectureEnrollment.of(command.getLectureId(), command.getLectureItemId(), command.getUserId(), enrolledAt);
        if (lectureEnrollmentRepository.existsByLectureIdAndUserId(command.getLectureId(), command.getUserId())) {
            throw new LectureAlreadyEnrolledException("해당 유저는 이미 수강신청을 했습니다.");
        }

        lectureRepository.saveItem(lectureItem);
        return lectureEnrollmentRepository.save(enrollment);
    }


    /**
     * 특정 유저가 특정한 특강에 신청했는지 확인한다.
     * @param lectureId 특강 ID
     * @param userId 유저 ID
     * @return 특강 신청 여부
     */
    public boolean checkEnrollment(final Long lectureId, final Long userId) {
        return lectureEnrollmentRepository.existsByLectureIdAndUserId(lectureId, userId);
    }

    /**
     * 사용자가 신청 가능한 강의 목록을 가져온다.
     * 수강 가능한 강의는 아직 수강 신청이 안된 강의 중에서, 잔여 수량이 있는 강의만을 의미한다.
     * @param userId 사용자 ID
     * @return 사용자가 수강 가능한 강의 목록
     */
    public List<LectureWithItems> getAvailableLectures(final Long userId) {
        final List<Lecture> availableLectures = lectureRepository.getAvailableLectures(userId);
        final Map<Long, List<LectureItem>> lectureItemMap = lectureRepository.getLectureItemMap(
                availableLectures.stream().map(Lecture::getId)
                        .toList());

        return availableLectures.stream()
                .map(lecture -> {
                    // 해당 강의의 아이템 리스트에서 remainingCapacity > 0인 것만 필터링
                    final List<LectureItem> availableItems = lectureItemMap.getOrDefault(lecture.getId(), List.of())
                            .stream()
                            .filter(item -> item.getRemainingCapacity() > 0)
                            .toList();
                    return LectureWithItems.of(lecture, availableItems);
                })
                .filter(lectureWithItems -> !lectureWithItems.getItems().isEmpty()) // 아이템이 있는 경우에만 강의를 포함
                .toList();
    }

    /**
     * 사용자가 신청한 모든 강의 목록을 가져온다.
     * @param userId 사용자 ID
     * @return 사용자가 신청한 모든 강의 목록
     */
    public List<LectureEnrollmentInfo> getUserLectureEnrollments(final Long userId) {
        // 사용자의 모든 강의 신청 기록을 가져옴
        final List<LectureEnrollment> enrollments = lectureEnrollmentRepository.findAllByUserId(userId);

        return enrollments.stream()
                .map(enrollment -> {
                    final Lecture lecture = lectureRepository.getById(enrollment.getLectureId());
                    final LectureItem item = lectureRepository.getItemById(enrollment.getLectureId(), enrollment.getLectureItemId());
                    return LectureEnrollmentInfo.of(lecture, item, enrollment);
                }).toList();
    }

}
