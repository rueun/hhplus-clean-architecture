package io.hhplus.cleanarchitecture.lecture.application.dto.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnrollLectureCommand {
    private Long lectureId;
    private Long lectureItemId;
    private Long userId;
}
