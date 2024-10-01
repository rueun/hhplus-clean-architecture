package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureEnrollmentJpaRepository extends JpaRepository<LectureEnrollmentJpaEntity, Long> {

    @Query("""
                SELECT e
                FROM LectureEnrollmentJpaEntity e
                WHERE e.lectureId = :lectureId
            """)
    List<LectureEnrollmentJpaEntity> findAllByLectureId(@Param("lectureId") final Long lectureId);

    @Query("""
                SELECT e
                FROM LectureEnrollmentJpaEntity e
                WHERE e.userId = :userId
            """)
    List<LectureEnrollmentJpaEntity> findAllByUserId(@Param("userId") final Long userId);
}
