package io.hhplus.cleanarchitecture.lecture.presentation.dto.req;

import io.hhplus.cleanarchitecture.lecture.application.dto.command.EnrollLectureCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnrollLectureRequest {
    private Long lectureItemId;
    private Long userId;

    public EnrollLectureCommand toCommand(final Long lectureId) {
        return EnrollLectureCommand.builder()
                .lectureId(lectureId)
                .lectureItemId(lectureItemId)
                .userId(userId)
                .build();
    }
}
