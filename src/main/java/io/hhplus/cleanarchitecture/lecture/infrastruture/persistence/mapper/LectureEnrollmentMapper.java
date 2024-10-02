package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LectureEnrollmentMapper {

    public static LectureEnrollmentJpaEntity toJpaEntity(final LectureEnrollment lectureEnrollment) {
        return LectureEnrollmentJpaEntity.builder()
                .id(lectureEnrollment.getId())
                .lectureId(lectureEnrollment.getLectureId())
                .lectureItemId(lectureEnrollment.getLectureItemId())
                .userId(lectureEnrollment.getUserId())
                .enrolledAt(lectureEnrollment.getEnrolledAt())
                .build();
    }

    public static LectureEnrollment toDomain(final LectureEnrollmentJpaEntity lectureEnrollmentJpaEntity) {
        return LectureEnrollment.builder()
                .id(lectureEnrollmentJpaEntity.getId())
                .lectureId(lectureEnrollmentJpaEntity.getLectureId())
                .lectureItemId(lectureEnrollmentJpaEntity.getLectureItemId())
                .userId(lectureEnrollmentJpaEntity.getUserId())
                .enrolledAt(lectureEnrollmentJpaEntity.getEnrolledAt())
                .build();
    }

}
