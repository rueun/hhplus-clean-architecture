package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;
    private final TimeProvider timeProvider;

    @Transactional
    public void enroll(final EnrollLectureCommand command) {
        final LocalDateTime now = timeProvider.now();

        final Lecture lecture = lectureRepository.getById(command.getLectureId());
        lecture.enrollStudent(now);

        final LectureEnrollment enrollment = LectureEnrollment.of(command.getLectureId(), command.getUserId(), now);
        if (lectureEnrollmentRepository.existsByLectureIdAndUserId(command.getLectureId(), command.getUserId())) {
            throw new IllegalArgumentException("해당 유저는 이미 수강신청을 했습니다.");
        }

        lectureRepository.save(lecture);
        lectureEnrollmentRepository.save(enrollment);
    }

    public boolean checkEnrollment(final Long lectureId, final Long userId) {
        return lectureEnrollmentRepository.existsByLectureIdAndUserId(lectureId, userId);
    }

    public Page<Lecture> getAvailableLectures(final Long userId, final Pageable pageable) {
        return lectureRepository.findAvailableLectures(userId, pageable);
    }
}
