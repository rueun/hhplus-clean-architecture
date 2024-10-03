package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:/application-test.yml")
class LectureEnrollmentRepositoryImplTest {

    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;

    private LectureEnrollmentRepositoryImpl lectureEnrollmentRepository;

    @BeforeEach
    public void setUp() {
        lectureEnrollmentRepository = new LectureEnrollmentRepositoryImpl(lectureEnrollmentJpaRepository);
    }

    @Test
    void 수강신청_내역을_저장할_수_있다() {
        // Given
        LectureEnrollment lectureEnrollment = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        LectureEnrollment savedLectureEnrollment = lectureEnrollmentRepository.save(lectureEnrollment);

        // Then
        assertAll(
                () -> then(savedLectureEnrollment.getId()).isNotNull(),
                () -> then(savedLectureEnrollment.getLectureId()).isEqualTo(1L),
                () -> then(savedLectureEnrollment.getLectureItemId()).isEqualTo(1L),
                () -> then(savedLectureEnrollment.getUserId()).isEqualTo(1L),
                () -> then(savedLectureEnrollment.getEnrolledAt()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @Test
    void 유저의_수강신청_내역을_조회할_수_있다() {
        // Given
        LectureEnrollment lectureEnrollment1 = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollment lectureEnrollment2 = LectureEnrollment.builder()
                .lectureId(2L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureEnrollmentRepository.save(lectureEnrollment1);
        lectureEnrollmentRepository.save(lectureEnrollment2);

        // When
        List<LectureEnrollment> userEnrollments = lectureEnrollmentRepository.findAllByUserId(1L);

        // Then
        assertAll (
                () -> then(userEnrollments.size()).isEqualTo(2),
                () -> assertThat(userEnrollments).extracting("lectureId", "userId", "enrolledAt")
                        .containsExactlyInAnyOrder(
                                tuple(1L, 1L, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(2L, 1L, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }

    @Test
    void 특정한_특강항목의_수강신청_내역을_조회할_수_있다() {
        // Given
        LectureEnrollment lectureEnrollment1 = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollment lectureEnrollment2 = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(2L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureEnrollmentRepository.save(lectureEnrollment1);
        lectureEnrollmentRepository.save(lectureEnrollment2);

        // When
        List<LectureEnrollment> lectureEnrollments = lectureEnrollmentRepository.findAllByLectureItemId(1L);

        // Then
        assertAll (
                () -> then(lectureEnrollments.size()).isEqualTo(2),
                () -> assertThat(lectureEnrollments).extracting("lectureId", "lectureItemId", "userId", "enrolledAt")
                        .containsExactlyInAnyOrder(
                                tuple(1L, 1L, 1L, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(1L, 1L, 2L, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }


    @Test
    void 유저가_특정_강의에_수강신청을_했는지_확인할_수_있다() {
        // Given
        LectureEnrollment lectureEnrollment1 = LectureEnrollment.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollment lectureEnrollment2 = LectureEnrollment.builder()
                .lectureId(2L)
                .lectureItemId(1L)
                .userId(2L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureEnrollmentRepository.save(lectureEnrollment1);
        lectureEnrollmentRepository.save(lectureEnrollment2);

        // When
        boolean exists1 = lectureEnrollmentRepository.existsByLectureIdAndUserId(1L, 1L);
        boolean exists2 = lectureEnrollmentRepository.existsByLectureIdAndUserId(2L, 1L);

        // Then
        then(exists1).isTrue();
        then(exists2).isFalse();
    }
}