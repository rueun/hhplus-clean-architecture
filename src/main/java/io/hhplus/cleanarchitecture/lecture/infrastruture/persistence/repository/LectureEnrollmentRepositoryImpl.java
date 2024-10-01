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

    private final EnrollmentJpaRepository enrollmentJpaRepository;

    @Override
    public LectureEnrollment save(final LectureEnrollment enrollment) {
        final LectureEnrollmentJpaEntity jpaEntity = enrollmentJpaRepository.save(LectureEnrollmentMapper.toJpaEntity(enrollment));
        return LectureEnrollmentMapper.toDomain(jpaEntity);
    }

    @Override
    public List<LectureEnrollment> findAllByUserId(final Long userId) {
        final List<LectureEnrollmentJpaEntity> jpaEntities = enrollmentJpaRepository.findAllByUserId(userId);
        return jpaEntities.stream()
                .map(LectureEnrollmentMapper::toDomain).toList();
    }

    @Override
    public boolean existsByLectureIdAndUserId(final Long lectureId, final Long userId) {
        return enrollmentJpaRepository.findAllByLectureId(lectureId)
                .stream()
                .anyMatch(lectureEnrollmentJpaEntity -> lectureEnrollmentJpaEntity.getUserId().equals(userId));
    }
}
