package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LectureMapper {

    public static LectureJpaEntity toJpaEntity(final Lecture lecture) {
        return LectureJpaEntity.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .speaker(lecture.getSpeaker())
                .maxEnrollmentCount(lecture.getMaxEnrollmentCount())
                .enrolledCount(lecture.getEnrolledCount())
                .enrollOpenAt(lecture.getEnrollOpenAt())
                .build();
    }

    public static Lecture toDomain(final LectureJpaEntity lectureJpaEntity) {
        return Lecture.builder()
                .id(lectureJpaEntity.getId())
                .title(lectureJpaEntity.getTitle())
                .speaker(lectureJpaEntity.getSpeaker())
                .maxEnrollmentCount(lectureJpaEntity.getMaxEnrollmentCount())
                .enrolledCount(lectureJpaEntity.getEnrolledCount())
                .enrollOpenAt(lectureJpaEntity.getEnrollOpenAt())
                .build();
    }
}
