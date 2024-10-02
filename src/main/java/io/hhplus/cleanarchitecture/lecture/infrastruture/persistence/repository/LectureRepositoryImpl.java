package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper.LectureItemMapper;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper.LectureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;
    private final LectureItemJpaRepository lectureItemJpaRepository;

    @Override
    public Lecture save(final Lecture lecture) {
        final LectureJpaEntity jpaEntity = lectureJpaRepository.save(LectureMapper.toJpaEntity(lecture));
        return LectureMapper.toDomain(jpaEntity);
    }

    @Override
    public LectureItem save(final LectureItem lectureItem) {
        final LectureItemJpaEntity jpaEntity = lectureItemJpaRepository.save(LectureItemMapper.toJpaEntity(lectureItem));
        return LectureItemMapper.toDomain(jpaEntity);
    }

    @Override
    public List<LectureItem> saveAll(final List<LectureItem> lectureItems) {
        List<LectureItemJpaEntity> jpaEntities = lectureItems.stream()
                .map(LectureItemMapper::toJpaEntity)
                .toList();

        final List<LectureItemJpaEntity> savedJpaEntities = lectureItemJpaRepository.saveAll(jpaEntities);
        return savedJpaEntities.stream()
                .map(LectureItemMapper::toDomain)
                .toList();
    }


    @Override
    public Lecture getById(final Long id) {
        LectureJpaEntity jpaEntity = lectureJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));
        return LectureMapper.toDomain(jpaEntity);
    }

    @Override
    public List<Lecture> getByIds(final List<Long> lectureIds) {
        List<LectureJpaEntity> jpaEntities = lectureJpaRepository.findAllById(lectureIds);
        return jpaEntities.stream()
                .map(LectureMapper::toDomain)
                .toList();
    }

    @Override
    public LectureItem getItemById(final Long lectureId, final Long itemId) {
        LectureItemJpaEntity jpaEntity = lectureItemJpaRepository.findByLectureIdAndId(lectureId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의 아이템을 찾을 수 없습니다."));
        return LectureItemMapper.toDomain(jpaEntity);
    }


    @Override
    public Map<Long, List<LectureItem>> getLectureItemMap(final List<Long> lectureIds) {
        List<LectureJpaEntity> lectureJpaEntities = lectureJpaRepository.findAllById(lectureIds);

        return lectureJpaEntities.stream()
                .collect(Collectors.toMap(
                        LectureJpaEntity::getId,
                        lectureJpaEntity -> {
                            List<LectureItemJpaEntity> lectureItemJpaEntities = lectureItemJpaRepository.findAllByLectureId(lectureJpaEntity.getId());
                            return lectureItemJpaEntities.stream()
                                    .map(LectureItemMapper::toDomain)
                                    .toList();
                        }
                ));
    }

    @Override
    public List<Lecture> getAvailableLectures(final Long userId) {
        final List<LectureJpaEntity> availableLectures = lectureJpaRepository.findAvailableLectures(userId);
        return availableLectures.stream()
                .map(LectureMapper::toDomain)
                .toList();
    }

}
