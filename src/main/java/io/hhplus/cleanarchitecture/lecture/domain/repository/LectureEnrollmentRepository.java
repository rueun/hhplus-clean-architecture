package io.hhplus.cleanarchitecture.lecture.domain.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;

import java.util.List;

public interface LectureEnrollmentRepository {
    LectureEnrollment save(LectureEnrollment enrollment);
    List<LectureEnrollment> findAllByUserId(Long userId);
    List<LectureEnrollment> findAllByLectureItemId(Long lectureItemId);
    boolean existsByLectureIdAndUserId(Long lectureId, Long userId);
}