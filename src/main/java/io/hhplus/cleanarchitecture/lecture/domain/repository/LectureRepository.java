package io.hhplus.cleanarchitecture.lecture.domain.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;

import java.util.List;
import java.util.Map;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    LectureItem saveItem(LectureItem lectureItem);
    List<LectureItem> saveAllItem(List<LectureItem> lectureItems);
    Lecture getById(Long lectureId);
    List<Lecture> getByIds(List<Long> lectureIds);
    LectureItem getItemById(Long lectureId, Long lectureItemId);
    List<LectureItem> getItemsByIds(List<Long> lectureItemIds);
    Map<Long, List<LectureItem>> getLectureItemMap(List<Long> lectureIds);
    LectureItem getItemByIdWithPessimisticLock(Long lectureId, Long lectureItemId);
    List<Lecture> getAvailableLectures(Long userId);
}
