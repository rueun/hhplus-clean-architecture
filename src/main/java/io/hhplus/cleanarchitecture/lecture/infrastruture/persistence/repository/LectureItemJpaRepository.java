package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureItemJpaRepository extends JpaRepository<LectureItemJpaEntity, Long> {

    Optional<LectureItemJpaEntity> findByLectureIdAndId(Long lectureId, Long id);

    List<LectureItemJpaEntity> findAllByLectureIdIn(List<Long> lectureIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT l FROM LectureItemJpaEntity l
            WHERE l.lectureId = :lectureId
            AND l.id = :lectureItemId
            """)
    Optional<LectureItemJpaEntity> findByIdWithPessimisticLock(@Param("lectureId") Long lectureId, @Param("lectureItemId") Long lectureItemId);
}
