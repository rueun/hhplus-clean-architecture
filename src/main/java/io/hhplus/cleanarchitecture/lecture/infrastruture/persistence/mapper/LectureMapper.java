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
                .instructor(lecture.getSpeaker())
                .build();
    }

    public static Lecture toDomain(final LectureJpaEntity lectureJpaEntity) {
        return Lecture.builder()
                .id(lectureJpaEntity.getId())
                .title(lectureJpaEntity.getTitle())
                .speaker(lectureJpaEntity.getInstructor())
                .build();
    }
}
