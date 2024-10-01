package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper.LectureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;
    private final TimeProvider timeProvider;

    @Override
    public Lecture save(final Lecture lecture) {
        final LectureJpaEntity jpaEntity = lectureJpaRepository.save(LectureMapper.toJpaEntity(lecture));
        return LectureMapper.toDomain(jpaEntity);
    }

    @Override
    public Lecture getById(final Long id) {
        LectureJpaEntity jpaEntity = lectureJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));
        return LectureMapper.toDomain(jpaEntity);
    }

    @Override
    public Page<Lecture> findAvailableLectures(final Long userId, final Pageable pageable) {
        return lectureJpaRepository.findAvailableLectures(userId, timeProvider.now(), pageable)
                .map(LectureMapper::toDomain);
    }
}
