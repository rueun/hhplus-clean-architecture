package io.hhplus.cleanarchitecture.lecture.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureEnrollment {
    private Long id;
    private Long lectureId;
    private Long lectureItemId;
    private Long userId;
    private LocalDateTime enrolledAt;

    public static LectureEnrollment of(final Long lectureId, final Long lectureItemId, final Long userId, final LocalDateTime enrolledAt) {
        return LectureEnrollment.builder()
                .lectureId(lectureId)
                .lectureItemId(lectureItemId)
                .userId(userId)
                .enrolledAt(enrolledAt)
                .build();
    }
}
