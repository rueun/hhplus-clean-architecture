package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureItemNotFoundException;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureNotFoundException;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:/application-test.yml")
class LectureRepositoryImplTest {

    @Autowired
    private LectureJpaRepository lectureJpaRepository;

    @Autowired
    private LectureItemJpaRepository lectureItemJpaRepository;

    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;

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
        assertAll(
                () -> then(lectureItems.size()).isEqualTo(2),
                () -> assertThat(lectureItems).extracting("lectureId", "capacity", "remainingCapacity", "lectureTime")
                        .containsExactlyInAnyOrder(
                                tuple(1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }

    @Test
    void 특정_ID로_강의를_가져올_수_있다() {
        // Given
        Lecture lecture = Lecture.builder()
                .title("클린 아키텍처")
                .instructor("로버트 C. 마틴")
                .build();
        final Lecture savedLecture = lectureRepository.save(lecture);

        // When
        final Lecture foundLecture = lectureRepository.getById(savedLecture.getId());

        // Then
        assertAll(
                () -> then(foundLecture.getId()).isEqualTo(savedLecture.getId()),
                () -> then(foundLecture.getTitle()).isEqualTo("클린 아키텍처"),
                () -> then(foundLecture.getInstructor()).isEqualTo("로버트 C. 마틴")
        );
    }

    @Test
    void 존재하지_않는_ID로_강의를_가져오려고_하면_예외가_발생한다() {
        // Given
        final Long notExistId = 999L;

        // When, Then
        // 예외 검사
        thenThrownBy(() -> lectureRepository.getById(notExistId))
                .isInstanceOf(LectureNotFoundException.class)
                .hasMessage("해당 강의를 찾을 수 없습니다.");
    }

    @Test
    void 여러_ID로_강의를_여러개_가져올_수_있다() {
        // Given
        Lecture lecture1 = Lecture.builder()
                .title("클린 아키텍처")
                .instructor("로버트 C. 마틴")
                .build();
        final Lecture savedLecture1 = lectureRepository.save(lecture1);

        Lecture lecture2 = Lecture.builder()
                .title("DDD")
                .instructor("에릭 에반스")
                .build();
        final Lecture savedLecture2 = lectureRepository.save(lecture2);

        // When
        final List<Lecture> foundLectures = lectureRepository.getByIds(List.of(savedLecture1.getId(), savedLecture2.getId()));

        // Then
        assertAll(
                () -> then(foundLectures.size()).isEqualTo(2),
                () -> assertThat(foundLectures).extracting("id", "title", "instructor")
                        .containsExactlyInAnyOrder(
                                tuple(savedLecture1.getId(), "클린 아키텍처", "로버트 C. 마틴"),
                                tuple(savedLecture2.getId(), "DDD", "에릭 에반스")
                        )
        );
    }

    @Test
    void 강의_항목_ID로_강의_항목을_가져올_수_있다() {
        // Given
        final LectureItem lectureItem = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        final LectureItem savedLectureItem = lectureRepository.saveItem(lectureItem);

        // When
        final LectureItem foundLectureItem = lectureRepository.getItemById(1L, savedLectureItem.getId());

        // Then
        assertAll(
                () -> then(foundLectureItem.getId()).isEqualTo(savedLectureItem.getId()),
                () -> then(foundLectureItem.getLectureId()).isEqualTo(1L),
                () -> then(foundLectureItem.getCapacity()).isEqualTo(30),
                () -> then(foundLectureItem.getRemainingCapacity()).isEqualTo(30),
                () -> then(foundLectureItem.getLectureTime()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @Test
    void 존재하지_않는_강의_항목_ID로_강의_항목을_가져오려고_하면_예외가_발생한다() {
        // Given
        final Long notExistLectureId = 999L;
        final Long notExistLectureItemId = 999L;

        // When, Then
        // 예외 검사
        thenThrownBy(() -> lectureRepository.getItemById(notExistLectureId, notExistLectureItemId))
                .isInstanceOf(LectureItemNotFoundException.class)
                .hasMessage("해당 강의 아이템을 찾을 수 없습니다.");
    }

    @Test
    void 여러_강의_항목_ID로_강의_항목을_여러개_가져올_수_있다() {
        // Given
        final LectureItem lectureItem1 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        final LectureItem savedLectureItem1 = lectureRepository.saveItem(lectureItem1);

        final LectureItem lectureItem2 = LectureItem.builder()
                .lectureId(1L)
                .capacity(20)
                .remainingCapacity(20)
                .lectureTime(LocalDateTime.parse("2024-10-02T10:00"))
                .build();
        final LectureItem savedLectureItem2 = lectureRepository.saveItem(lectureItem2);

        // When
        final List<LectureItem> foundLectureItems = lectureRepository.getItemsByIds(List.of(savedLectureItem1.getId(), savedLectureItem2.getId()));

        // Then
        assertAll(
                () -> then(foundLectureItems.size()).isEqualTo(2),
                () -> assertThat(foundLectureItems).extracting("id", "lectureId", "capacity", "remainingCapacity", "lectureTime")
                        .containsExactlyInAnyOrder(
                                tuple(savedLectureItem1.getId(), 1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(savedLectureItem2.getId(), 1L, 20, 20, LocalDateTime.parse("2024-10-02T10:00"))
                        )
        );
    }

    @Test
    void 여러_강의_ID로_각_강의마다_항목들을_가져올_수_있다() {
        // Given
        final LectureItem lectureItem1 = LectureItem.builder()
                .lectureId(1L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        final LectureItem savedLectureItem1 = lectureRepository.saveItem(lectureItem1);

        final LectureItem lectureItem2 = LectureItem.builder()
                .lectureId(1L)
                .capacity(20)
                .remainingCapacity(20)
                .lectureTime(LocalDateTime.parse("2024-10-02T10:00"))
                .build();
        final LectureItem savedLectureItem2 = lectureRepository.saveItem(lectureItem2);

        final LectureItem lectureItem3 = LectureItem.builder()
                .lectureId(2L)
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        final LectureItem savedLectureItem3 = lectureRepository.saveItem(lectureItem3);

        // When
        final Map<Long, List<LectureItem>> lectureItemMap = lectureRepository.getLectureItemMap(List.of(1L, 2L));

        // Then
        assertAll(
                () -> then(lectureItemMap.size()).isEqualTo(2),
                () -> assertThat(lectureItemMap.get(1L)).extracting("id", "lectureId", "capacity", "remainingCapacity", "lectureTime")
                        .containsExactlyInAnyOrder(
                                tuple(savedLectureItem1.getId(), 1L, 30, 30, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(savedLectureItem2.getId(), 1L, 20, 20, LocalDateTime.parse("2024-10-02T10:00"))
                        ),
                () -> assertThat(lectureItemMap.get(2L)).extracting("id", "lectureId", "capacity", "remainingCapacity", "lectureTime")
                        .containsExactlyInAnyOrder(
                                tuple(savedLectureItem3.getId(), 2L, 30, 30, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }

    @Test
    void 유저별_신청가능한_강의_목록을_조회할_수_있다() {
        // Given
        final LectureJpaEntity lecture1 = lectureJpaRepository.save(
                LectureJpaEntity.builder()
                .title("클린 아키텍처")
                .instructor("로버트 C. 마틴")
                .build());

        final LectureJpaEntity lecture2 = lectureJpaRepository.save(
                LectureJpaEntity.builder()
                        .title("DDD")
                        .instructor("에릭 에반스")
                        .build());

        final LectureItemJpaEntity lectureItem1 = lectureItemJpaRepository.save(
                LectureItemJpaEntity.builder()
                .lectureId(lecture1.getId())
                .capacity(30)
                .remainingCapacity(30)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build());

        lectureItemJpaRepository.save(
                LectureItemJpaEntity.builder()
                        .lectureId(lecture1.getId())
                        .capacity(20)
                        .remainingCapacity(20)
                        .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        lectureEnrollmentJpaRepository.save(
                LectureEnrollmentJpaEntity.builder()
                        .lectureItemId(lectureItem1.getId())
                        .userId(1L)
                        .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                        .build());

        // When
        final List<Lecture> availableLectures = lectureRepository.getAvailableLectures(1L);

        // Then
        assertAll(
                () -> then(availableLectures.size()).isEqualTo(1),
                () -> assertThat(availableLectures).extracting("id", "title", "instructor")
                        .containsExactlyInAnyOrder(
                                tuple(lecture2.getId(), "DDD", "에릭 에반스")
                        )
        );


    }

}