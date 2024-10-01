package io.hhplus.cleanarchitecture.lecture.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Lecture {
    private Long id;
    private String title;
    private String speaker;
    private int maxEnrollmentCount = 30;
    private int enrolledCount = 0;
    private LocalDateTime enrollOpenAt;

    public void enrollStudent(final LocalDateTime enrollTime) {
        if (enrolledCount >= maxEnrollmentCount) {
            throw new IllegalStateException("최대 수강 인원을 초과했습니다.");
        }

        if (enrollOpenAt.isAfter(enrollTime)) {
            throw new IllegalStateException("수강 신청 기간이 아닙니다.");
        }

        this.enrolledCount++;
    }
}
