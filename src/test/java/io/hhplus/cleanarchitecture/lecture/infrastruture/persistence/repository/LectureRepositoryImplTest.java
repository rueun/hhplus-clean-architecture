package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:/application-test.yml")
class LectureRepositoryImplTest {

    @Autowired
    private LectureJpaRepository lectureJpaRepository;

    @Autowired
    private LectureItemJpaRepository lectureItemJpaRepository;

    private LectureRepositoryImpl lectureRepository;

    @BeforeEach
    public void setUp() {
        lectureRepository = new LectureRepositoryImpl(lectureJpaRepository, lectureItemJpaRepository);
    }

    @Test
    void 강의를_저장할_수_있다() {
        // Given
        Lecture lecture = Lecture.builder()
                .title("클린 아키텍처")
                .instructor("로버트 C. 마틴")
                .build();

        // When
        Lecture savedLecture = lectureRepository.save(lecture);

        // Then
        // 한 번에 검사
        assertAll(
                () -> then(savedLecture.getId()).isNotNull(),
                () -> then(savedLecture.getTitle()).isEqualTo("클린 아키텍처"),
                () -> then(savedLecture.getInstructor()).isEqualTo("로버트 C. 마틴")
        );
    }

    @Test
    void 강의_아이템을_저장할_수_있다() {
        // Given
        final LectureItem lectureItem = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        final LectureItem savedLectureItem = lectureRepository.saveItem(lectureItem);

        // Then
        // 한 번에 검사
        assertAll(
                () -> then(savedLectureItem.getId()).isNotNull(),
                () -> then(savedLectureItem.getLectureId()).isEqualTo(1L),
                () -> then(savedLectureItem.getCapacity()).isEqualTo(30),
                () -> then(savedLectureItem.getRemainingCapacity()).isEqualTo(30),
                () -> then(savedLectureItem.getLectureTime()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @Test
    void 강의_아이템을_여러개_저장할_수_있다() {
        // Given
        final LectureItem lectureItem1 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        final LectureItem lectureItem2 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        final List<LectureItem> lectureItems = lectureRepository.saveAllItem(List.of(lectureItem1, lectureItem2));

        // Then
        // 한 번에 검사
        assertAll(
                () -> then(lectureItems.size()).isEqualTo(2),
                () -> assertThat(lectureItems).extracting("lectureId", "capacity", "remainingCapacity", "lectureTime")
                        .containsExactlyInAnyOrder(
                                tuple(1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }
}