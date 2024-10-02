package io.hhplus.cleanarchitecture.lecture.presentation.dto.resp;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.hhplus.cleanarchitecture.lecture.application.dto.LectureEnrollmentInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserEnrollmentResponse {
    private String title;
    private String instructor;
    private Long lectureId;
    private Long lectureItemId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lectureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrolledAt;

    public static UserEnrollmentResponse of(final LectureEnrollmentInfo lectureEnrollmentInfo) {
        return UserEnrollmentResponse.builder()
                .title(lectureEnrollmentInfo.getLecture().getTitle())
                .instructor(lectureEnrollmentInfo.getLecture().getInstructor())
                .lectureId(lectureEnrollmentInfo.getLectureItem().getLectureId())
                .lectureItemId(lectureEnrollmentInfo.getLectureItem().getId())
                .lectureTime(lectureEnrollmentInfo.getLectureItem().getLectureTime())
                .enrolledAt(lectureEnrollmentInfo.getLectureEnrollment().getEnrolledAt())
                .build();
    }
}
