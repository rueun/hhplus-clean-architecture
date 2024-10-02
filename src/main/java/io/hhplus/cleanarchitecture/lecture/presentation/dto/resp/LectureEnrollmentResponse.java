package io.hhplus.cleanarchitecture.lecture.presentation.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LectureEnrollmentResponse {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrolledAt;

    public static LectureEnrollmentResponse of(final LectureEnrollment lectureEnrollment) {
        return LectureEnrollmentResponse.builder()
                .id(lectureEnrollment.getId())
                .enrolledAt(lectureEnrollment.getEnrolledAt())
                .build();
    }
}
