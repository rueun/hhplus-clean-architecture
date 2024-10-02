package io.hhplus.cleanarchitecture.lecture.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureItem {
    private static final int DEFAULT_CAPACITY = 30;

    private Long id;
    private Long lectureId;
    private int capacity = DEFAULT_CAPACITY;
    private int remainingCapacity = DEFAULT_CAPACITY;
    private LocalDateTime lectureTime;


    public void enroll(final LocalDateTime enrolledAt) {
        if (remainingCapacity == 0) {
            throw new IllegalStateException("잔여 수량이 없습니다.");
        }

        if (lectureTime.isBefore(enrolledAt)) {
            throw new IllegalStateException("수강 신청 시간이 지났습니다.");
        }

        this.remainingCapacity--;
    }
}
