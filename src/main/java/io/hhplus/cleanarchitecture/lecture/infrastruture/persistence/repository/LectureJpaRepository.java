package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {

    @Query("""
           SELECT l
           FROM LectureJpaEntity l
           LEFT JOIN LectureEnrollmentJpaEntity e ON l.id = e.lectureId AND e.userId = :userId
           WHERE l.enrollOpenAt <= :currentDate
           AND (e.userId IS NULL)
           """)
    Page<LectureJpaEntity> findAvailableLectures(@Param("userId") Long userId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);
}
