package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LectureItemMapper {

    public static LectureItemJpaEntity toJpaEntity(final LectureItem lectureItem) {
        return LectureItemJpaEntity.builder()
                .id(lectureItem.getId())
                .lectureId(lectureItem.getLectureId())
                .capacity(lectureItem.getCapacity())
                .remainingCapacity(lectureItem.getRemainingCapacity())
                .lectureTime(lectureItem.getLectureTime())
                .build();
    }

    public static LectureItem toDomain(final LectureItemJpaEntity lectureJpaEntity) {
        return LectureItem.builder()
                .id(lectureJpaEntity.getId())
                .lectureId(lectureJpaEntity.getLectureId())
                .capacity(lectureJpaEntity.getCapacity())
                .remainingCapacity(lectureJpaEntity.getRemainingCapacity())
                .lectureTime(lectureJpaEntity.getLectureTime())
                .build();
    }
}
