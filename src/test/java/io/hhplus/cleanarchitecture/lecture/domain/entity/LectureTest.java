package io.hhplus.cleanarchitecture.lecture.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class LectureTest {

    @ParameterizedTest
    @ValueSource(strings = {"2024-10-01T10:00", "2024-10-01T10:01"})
    void 최대_수강인원을_초과하지_않고_수강_신청이_가능한_날짜이면_수강인원_증가시킨다(String enrollTimeStr) {
        // Given
        Lecture lecture = Lecture.builder()
                .maxEnrollmentCount(30)
                .enrolledCount(0)
                .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LocalDateTime enrollTime = LocalDateTime.parse(enrollTimeStr);

        // When
        lecture.enrollStudent(enrollTime);

        // Then
        then(lecture.getEnrolledCount()).isEqualTo(1);
    }

    @Test
    void 최대_수강인원을_초과하는_경우_예외가_발생한다() {
        // Given
        Lecture lecture = Lecture.builder()
                .maxEnrollmentCount(30)
                .enrolledCount(30)
                .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LocalDateTime enrollTime = LocalDateTime.parse("2024-10-01T10:00");

        // When & Then
        thenThrownBy(() -> lecture.enrollStudent(enrollTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("최대 수강 인원을 초과했습니다.");
    }

    @Test
    void 수강신청이_열리지_않았을_때_예외가_발생한다() {
        // Given
        Lecture lecture = Lecture.builder()
                .maxEnrollmentCount(30)
                .enrolledCount(0)
                .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LocalDateTime enrollTime = LocalDateTime.parse("2024-10-01T09:59");

        // When & Then
        thenThrownBy(() -> lecture.enrollStudent(enrollTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수강 신청 기간이 아닙니다.");
    }
}