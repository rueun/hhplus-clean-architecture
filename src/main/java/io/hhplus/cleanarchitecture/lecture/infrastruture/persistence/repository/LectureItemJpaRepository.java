package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureItemJpaRepository extends JpaRepository<LectureItemJpaEntity, Long> {

    Optional<LectureItemJpaEntity> findByLectureIdAndId(@Param("lectureId") Long lectureId, @Param("id") Long id);

    List<LectureItemJpaEntity> findAllByLectureId(@Param("lectureId") Long lectureId);
}
