package io.hhplus.cleanarchitecture.lecture.presentation.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureItemResponse {
    private Long id;
    private int capacity;
    private int remainingCapacity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lectureTime;

    public static LectureItemResponse of(final LectureItem lectureItem) {
        return LectureItemResponse.builder()
                .id(lectureItem.getId())
                .capacity(lectureItem.getCapacity())
                .remainingCapacity(lectureItem.getRemainingCapacity())
                .lectureTime(lectureItem.getLectureTime())
                .build();
    }
}
