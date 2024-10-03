package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LectureEnrollmentJpaRepository extends JpaRepository<LectureEnrollmentJpaEntity, Long> {

    @Query("""
                SELECT e
                FROM LectureEnrollmentJpaEntity e
                WHERE e.userId = :userId
            """)
    List<LectureEnrollmentJpaEntity> findAllByUserId(@Param("userId") final Long userId);


    @Query("""
                SELECT e
                FROM LectureEnrollmentJpaEntity e
                WHERE e.lectureItemId = :lectureItemId
            """)
    List<LectureEnrollmentJpaEntity> findAllByLectureItemId(@Param("lectureItemId") final Long lectureItemId);


    Optional<LectureEnrollmentJpaEntity> findByLectureIdAndUserId(@Param("lectureId") final Long lectureId, @Param("userId") final Long userId);
}
