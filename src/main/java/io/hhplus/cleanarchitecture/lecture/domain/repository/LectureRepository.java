package io.hhplus.cleanarchitecture.lecture.domain.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    Lecture getById(Long id);

    Page<Lecture> findAvailableLectures(Long userId, Pageable pageable);
}
