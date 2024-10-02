package io.hhplus.cleanarchitecture.lecture.presentation.dto.resp;


import io.hhplus.cleanarchitecture.lecture.application.dto.LectureWithItems;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AvailableLectureForUserResponse {
    private Long lectureId;
    private String title;
    private String instructor;
    private List<LectureItemResponse> items;

    public static AvailableLectureForUserResponse of(final LectureWithItems lectureWithItems) {
        return AvailableLectureForUserResponse.builder()
                .lectureId(lectureWithItems.getId())
                .title(lectureWithItems.getTitle())
                .instructor(lectureWithItems.getInstructor())
                .items(lectureWithItems.getItems().stream()
                        .map(LectureItemResponse::of)
                        .toList())
                .build();
    }

}
