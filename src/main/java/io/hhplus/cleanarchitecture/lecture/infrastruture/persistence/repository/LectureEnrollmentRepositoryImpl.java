package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.mapper.LectureEnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LectureEnrollmentRepositoryImpl implements LectureEnrollmentRepository {

    private final LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;

    @Override
    public LectureEnrollment save(final LectureEnrollment enrollment) {
        final LectureEnrollmentJpaEntity jpaEntity = lectureEnrollmentJpaRepository.save(LectureEnrollmentMapper.toJpaEntity(enrollment));
        return LectureEnrollmentMapper.toDomain(jpaEntity);
    }

    @Override
    public List<LectureEnrollment> findAllByUserId(final Long userId) {
        final List<LectureEnrollmentJpaEntity> jpaEntities = lectureEnrollmentJpaRepository.findAllByUserId(userId);
        return jpaEntities.stream()
                .map(LectureEnrollmentMapper::toDomain).toList();
    }

    @Override
    public List<LectureEnrollment> findAllByLectureItemId(final Long lectureItemId) {
        final List<LectureEnrollmentJpaEntity> jpaEntities = lectureEnrollmentJpaRepository.findAllByLectureItemId(lectureItemId);
        return jpaEntities.stream()
                .map(LectureEnrollmentMapper::toDomain).toList();
    }

    @Override
    public boolean existsByLectureIdAndUserId(final Long lectureId, final Long userId) {
        return lectureEnrollmentJpaRepository.findAllByLectureId(lectureId)
                .stream()
                .anyMatch(lectureEnrollmentJpaEntity -> lectureEnrollmentJpaEntity.getUserId().equals(userId));
    }
}
