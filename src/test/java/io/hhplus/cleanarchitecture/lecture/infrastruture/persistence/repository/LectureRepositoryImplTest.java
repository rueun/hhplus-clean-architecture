package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.common.time.SystemTimeProvider;
import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ContextConfiguration(classes = LectureRepositoryImplTest.TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:/application-test.yml")
class LectureRepositoryImplTest {

    @Autowired
    private LectureJpaRepository lectureJpaRepository;

    @Autowired
    private TimeProvider timeProvider;

    private LectureRepositoryImpl lectureRepository;

    @BeforeEach
    public void setUp() {
        lectureRepository = new LectureRepositoryImpl(lectureJpaRepository, timeProvider);
    }

    @Test
    void 강의를_저장할_수_있다() {
        // Given
        Lecture lecture = Lecture.builder()
                .title("클린 아키텍처")
                .speaker("로버트 C. 마틴")
                .maxEnrollmentCount(30)
                .enrolledCount(0)
                .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        Lecture savedLecture = lectureRepository.save(lecture);

        // Then
        // 한 번에 검사
        assertAll(
                () -> then(savedLecture.getId()).isNotNull(),
                () -> then(savedLecture.getTitle()).isEqualTo("클린 아키텍처"),
                () -> then(savedLecture.getSpeaker()).isEqualTo("로버트 C. 마틴"),
                () -> then(savedLecture.getMaxEnrollmentCount()).isEqualTo(30),
                () -> then(savedLecture.getEnrolledCount()).isEqualTo(0),
                () -> then(savedLecture.getEnrollOpenAt()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @Test
    void 강의_ID로_강의를_조회할_수_있다() {
        // Given
        Lecture lecture = Lecture.builder()
                .title("클린 아키텍처")
                .speaker("로버트 C. 마틴")
                .maxEnrollmentCount(30)
                .enrolledCount(0)
                .enrollOpenAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        // When
        Lecture foundLecture = lectureRepository.getById(savedLecture.getId());

        // Then
        // 한 번에 검사
        assertAll(
                () -> then(foundLecture.getId()).isEqualTo(savedLecture.getId()),
                () -> then(foundLecture.getTitle()).isEqualTo("클린 아키텍처"),
                () -> then(foundLecture.getSpeaker()).isEqualTo("로버트 C. 마틴"),
                () -> then(foundLecture.getMaxEnrollmentCount()).isEqualTo(30),
                () -> then(foundLecture.getEnrolledCount()).isEqualTo(0),
                () -> then(foundLecture.getEnrollOpenAt()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TimeProvider timeProvider() {
            return new SystemTimeProvider();
        }
    }
}