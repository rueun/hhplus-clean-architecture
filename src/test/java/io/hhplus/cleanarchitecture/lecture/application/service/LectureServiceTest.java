package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
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
                .userId(1L)
                .build();

        given(lectureRepository.getById(1L)).willReturn(
                Lecture.builder()
                        .id(1L)
                        .maxEnrollmentCount(30)
                        .enrolledCount(29)
                        .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:00"));

        // When
        lectureService.enroll(command);

        // Then
        verify(lectureRepository).getById(1L);
        verify(lectureRepository).save(any(Lecture.class));
        verify(lectureEnrollmentRepository).save(any(LectureEnrollment.class));
    }

    @Test
    void 이미_수강신청한_사용자는_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getById(1L)).willReturn(
                Lecture.builder()
                        .id(1L)
                        .maxEnrollmentCount(30)
                        .enrolledCount(29)
                        .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:00"));

        given(lectureEnrollmentRepository.existsByLectureIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 유저는 이미 수강신청을 했습니다.");
    }

    @Test
    void 수강신청_기간이_아닌_경우_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getById(1L)).willReturn(
                Lecture.builder()
                        .id(1L)
                        .maxEnrollmentCount(30)
                        .enrolledCount(29)
                        .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T09:59"));

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수강 신청 기간이 아닙니다.");
    }

    @Test
    void 수강신청_인원이_초과된_경우_수강신청을_할_수_없다() {
        // Given
        EnrollLectureCommand command = EnrollLectureCommand.builder()
                .lectureId(1L)
                .userId(1L)
                .build();

        given(lectureRepository.getById(1L)).willReturn(
                Lecture.builder()
                        .id(1L)
                        .maxEnrollmentCount(30)
                        .enrolledCount(30)
                        .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        given(timeProvider.now()).willReturn(LocalDateTime.parse("2024-10-01T10:00"));

        // When & Then
        thenThrownBy(() -> lectureService.enroll(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("최대 수강 인원을 초과했습니다.");
    }
}