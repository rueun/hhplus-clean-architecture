package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureJpaRepository extends JpaRepository<LectureJpaEntity, Long> {

    @Query(
            """
                SELECT l
                FROM LectureJpaEntity l
                WHERE l.id NOT IN (
                    SELECT i.lectureId
                    FROM LectureItemJpaEntity i
                    JOIN LectureEnrollmentJpaEntity e ON i.id = e.lectureItemId
                    WHERE e.userId = :userId
                )
    """)
    List<LectureJpaEntity> findAvailableLectures(@Param("userId") Long userId);
}
