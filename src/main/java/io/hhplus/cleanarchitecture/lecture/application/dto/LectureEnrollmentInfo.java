package io.hhplus.cleanarchitecture.lecture.application.dto;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureEnrollmentInfo {
    private Lecture lecture;
    private LectureItem lectureItem;
    private LectureEnrollment lectureEnrollment;

    public static LectureEnrollmentInfo of(final Lecture lecture, final LectureItem lectureItem, final LectureEnrollment lectureEnrollment) {
        return LectureEnrollmentInfo.builder()
                .lecture(lecture)
                .lectureItem(lectureItem)
                .lectureEnrollment(lectureEnrollment)
                .build();
    }
}
