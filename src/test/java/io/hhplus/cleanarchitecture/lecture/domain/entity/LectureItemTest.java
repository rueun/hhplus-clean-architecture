package io.hhplus.cleanarchitecture.lecture.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class LectureItemTest {

    @ParameterizedTest
    @ValueSource(strings = {"2024-10-01T10:00", "2024-10-01T09:59"})
    void 최대_수강인원을_초과하지_않고_수강_신청이_가능한_시간이면_수강신청에_성공한다(String enrollTimeStr) {
        // Given
        LectureItem lectureItem = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        lectureItem.enroll(LocalDateTime.parse(enrollTimeStr));

        // Then
        then(lectureItem.getRemainingCapacity()).isEqualTo(29);
    }

    @Test
    void 강의의_잔여_수량이_초과하는_경우_에러가_발생한다() {
        // Given
        LectureItem lectureItem = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(0)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When & Then
        thenThrownBy(() -> lectureItem.enroll(LocalDateTime.parse("2024-10-01T10:00")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("잔여 수량이 없습니다.");
    }

    @Test
    void 강의_시간이_지나면_강의를_신청할_수_없다() {
        // Given
        LectureItem lectureItem = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When & Then
        thenThrownBy(() -> lectureItem.enroll(LocalDateTime.parse("2024-10-01T10:01")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수강 신청 시간이 지났습니다.");
    }
}