package io.hhplus.cleanarchitecture.lecture.application.dto;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureWithItems {
    private Long id;
    private String title;
    private String instructor;
    private List<LectureItem> items;

    public static LectureWithItems of(final Lecture lecture, final List<LectureItem> lectureItems) {
        return LectureWithItems.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .instructor(lecture.getInstructor())
                .items(lectureItems)
                .build();
    }
}
