package io.hhplus.cleanarchitecture.lecture.domain.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;

import java.util.List;
import java.util.Map;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    LectureItem save(LectureItem lectureItem);
    List<LectureItem> saveAll(List<LectureItem> lectureItems);
    Lecture getById(Long lectureId);
    List<Lecture> getByIds(List<Long> lectureIds);
    LectureItem getItemById(Long lectureId, Long lectureItemId);
    Map<Long, List<LectureItem>> getLectureItemMap(List<Long> lectureIds);
    List<Lecture> getAvailableLectures(Long userId);
}
