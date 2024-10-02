package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureEnrollmentInfo;
import io.hhplus.cleanarchitecture.lecture.application.dto.command.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureWithItems;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureAlreadyEnrolledException;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureCapacityExceededException;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureEnrollmentClosedException;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureEnrollmentRepository lectureEnrollmentRepository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private LectureService lectureService;


    @Test
    void 사용자는_특강_신청을_할_수_있다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getItemById(1L, 1L)).willReturn(
                LectureItem.builder()
                        .id(1L)
                        .capacity(30)
                        .remainingCapacity(30)
                        .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:00"));

        // When
        lectureService.enroll(command);

        // Then
        assertAll(
                () -> verify(lectureRepository).getItemById(1L, 1L),
                () -> verify(lectureRepository).saveItem(any(LectureItem.class)),
                () -> verify(lectureEnrollmentRepository).save(any(LectureEnrollment.class))
        );
    }

    @Test
    void 이미_수강신청한_사용자는_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getItemById(1L, 1L)).willReturn(
                LectureItem.builder()
                        .id(1L)
                        .capacity(30)
                        .remainingCapacity(29)
                        .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:00"));

        given(lectureEnrollmentRepository.existsByLectureIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(LectureAlreadyEnrolledException.class)
                .hasMessage("해당 유저는 이미 수강신청을 했습니다.");
    }


    @Test
    void 수강신청_시간이_지난_경우_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getItemById(1L, 1L)).willReturn(
                LectureItem.builder()
                        .id(1L)
                        .capacity(30)
                        .remainingCapacity(29)
                        .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:01"));

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(LectureEnrollmentClosedException.class)
                .hasMessage("수강 신청 시간이 지났습니다.");
    }

    @Test
    void 수강신청_인원이_초과된_경우_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getItemById(1L, 1L)).willReturn(
                LectureItem.builder()
                        .id(1L)
                        .capacity(30)
                        .remainingCapacity(0)
                        .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(LectureCapacityExceededException.class)
                .hasMessage("잔여 수량이 없습니다.");
    }

    @Test
    void 사용자는_신청_가능한_강의_목록을_가져올_수_있다() {
        // Given
        Lecture lecture = Lecture.builder()
                .title("특강1")
                .id(1L)
                .build();

        LectureItem lectureItem1 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(20)
                .build();

        LectureItem lectureItem2 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(0)
                .build();

        given(lectureRepository.getAvailableLectures(1L)).willReturn(List.of(lecture));
        given(lectureRepository.getLectureItemMap(List.of(1L))).willReturn(Map.of(
                1L, List.of(lectureItem1, lectureItem2)
        ));

        // When
        List<LectureWithItems> availableLectures = lectureService.getAvailableLectures(1L);

        // Then
        assertAll(
                () -> assertEquals(1, availableLectures.size()),
                () -> assertEquals(1, availableLectures.get(0).getItems().size()), // 잔여 수량이 있는 강의 아이템만 포함되어야 함
                () -> assertThat(availableLectures.get(0).getItems()).extracting("lectureId", "capacity", "remainingCapacity")
                        .containsExactly(
                                tuple(1L, 30, 20)
                        )
        );
    }

    @Test
    void 사용자는_신청한_모든_강의_목록을_가져올_수_있다() {
        // Given
        Long userId = 1L;

        Lecture lecture1 = Lecture.builder().id(1L).title("특강1").build();
        Lecture lecture2 = Lecture.builder().id(2L).title("특강2").build();

        LectureItem lectureItem1 = LectureItem.builder()
                .id(1L)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        LectureItem lectureItem2 = LectureItem.builder()
                .id(2L)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollment enrollment1 = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        LectureEnrollment enrollment2 = LectureEnrollment.builder()
                .lectureId(2L)
                .lectureItemId(2L)
                .build();

        // Mocking repository responses
        given(lectureEnrollmentRepository.findAllByUserId(userId)).willReturn(List.of(enrollment1, enrollment2));
        given(lectureRepository.getById(1L)).willReturn(lecture1);
        given(lectureRepository.getById(2L)).willReturn(lecture2);
        given(lectureRepository.getItemById(1L, 1L)).willReturn(lectureItem1);
        given(lectureRepository.getItemById(2L, 2L)).willReturn(lectureItem2);

        // When
        List<LectureEnrollmentInfo> userLectureEnrollments = lectureService.getUserLectureEnrollments(userId);

        // Then
        assertAll(
                () -> assertEquals(2, userLectureEnrollments.size()),
                () -> {
                    // 첫 번째 강의 검증
                    final LectureEnrollmentInfo firstLectureEnrollment = userLectureEnrollments.get(0);
                    assertEquals("특강1", firstLectureEnrollment.getLecture().getTitle());
                    assertEquals(1L, firstLectureEnrollment.getLecture().getId());
                    assertEquals(LocalDateTime.parse("2024-10-01T10:00"), firstLectureEnrollment.getLectureItem().getLectureTime());

                    // 두 번째 강의 검증
                    final LectureEnrollmentInfo secondLectureEnrollment = userLectureEnrollments.get(1);
                    assertEquals("특강2", secondLectureEnrollment.getLecture().getTitle());
                    assertEquals(2L, secondLectureEnrollment.getLecture().getId());
                    assertEquals(LocalDateTime.parse("2024-10-01T10:00"), secondLectureEnrollment.getLectureItem().getLectureTime());
                }
        );
    }
}