package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureEnrollmentJpaEntity;
import io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity.LectureItemJpaEntity;
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
    private LectureItemJpaRepository lectureItemJpaRepository;

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
        LectureEnrollmentJpaEntity lectureEnrollment = LectureEnrollmentJpaEntity.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        // When
        final LectureEnrollmentJpaEntity savedLectureEnrollment = lectureEnrollmentJpaRepository.save(lectureEnrollment);

        // Then
        assertAll(
                () -> then(savedLectureEnrollment.getId()).isNotNull(),
                () -> then(savedLectureEnrollment.getLectureItemId()).isEqualTo(1L),
                () -> then(savedLectureEnrollment.getUserId()).isEqualTo(1L),
                () -> then(savedLectureEnrollment.getEnrolledAt()).isEqualTo(LocalDateTime.parse("2024-10-01T10:00"))
        );
    }

    @Test
    void 유저의_수강신청_내역을_조회할_수_있다() {
        // Given
        LectureEnrollmentJpaEntity lectureEnrollment1 = LectureEnrollmentJpaEntity.builder()
                .lectureId(1L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollmentJpaEntity lectureEnrollment2 = LectureEnrollmentJpaEntity.builder()
                .lectureId(2L)
                .lectureItemId(2L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureEnrollmentJpaRepository.save(lectureEnrollment1);
        lectureEnrollmentJpaRepository.save(lectureEnrollment2);

        // When
        List<LectureEnrollment> userEnrollments = lectureEnrollmentRepository.findAllByUserId(1L);

        // Then
        assertAll (
                () -> then(userEnrollments.size()).isEqualTo(2),
                () -> assertThat(userEnrollments).extracting("lectureItemId", "userId", "enrolledAt")
                        .containsExactlyInAnyOrder(
                                tuple(1L, 1L, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple(2L, 1L, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }

    @Test
    void 특정한_특강항목의_수강신청_내역을_조회할_수_있다() {
        // Given
        LectureEnrollmentJpaEntity lectureEnrollment1 = LectureEnrollmentJpaEntity.builder()
                .lectureItemId(1L)
                .lectureId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollmentJpaEntity lectureEnrollment2 = LectureEnrollmentJpaEntity.builder()
                .lectureItemId(1L)
                .lectureId(1L)
                .userId(2L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureEnrollmentJpaRepository.save(lectureEnrollment1);
        lectureEnrollmentJpaRepository.save(lectureEnrollment2);

        // When
        List<LectureEnrollment> lectureEnrollments = lectureEnrollmentRepository.findAllByLectureItemId(1L);

        // Then
        assertAll (
                () -> then(lectureEnrollments.size()).isEqualTo(2),
                () -> assertThat(lectureEnrollments).extracting("lectureItemId", "userId", "enrolledAt")
                        .containsExactlyInAnyOrder(
                                tuple( 1L, 1L, LocalDateTime.parse("2024-10-01T10:00")),
                                tuple( 1L, 2L, LocalDateTime.parse("2024-10-01T10:00"))
                        )
        );
    }


    @Test
    void 유저가_특정_강의에_수강신청을_했는지_확인할_수_있다() {
        // Given
        LectureItemJpaEntity lectureItem1 = LectureItemJpaEntity.builder()
                .lectureId(100L)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureItemJpaEntity lectureItem2= LectureItemJpaEntity.builder()
                .lectureId(200L)
                .lectureTime(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollmentJpaEntity lectureEnrollment1 = LectureEnrollmentJpaEntity.builder()
                .lectureId(100L)
                .lectureItemId(1L)
                .userId(1L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        LectureEnrollmentJpaEntity lectureEnrollment2 = LectureEnrollmentJpaEntity.builder()
                .lectureId(200L)
                .lectureItemId(2L)
                .userId(2L)
                .enrolledAt(LocalDateTime.parse("2024-10-01T10:00"))
                .build();

        lectureItemJpaRepository.saveAll(List.of(lectureItem1, lectureItem2));
        lectureEnrollmentJpaRepository.save(lectureEnrollment1);
        lectureEnrollmentJpaRepository.save(lectureEnrollment2);


        // When
        boolean exists1 = lectureEnrollmentRepository.existsByLectureIdAndUserId(100L, 1L);
        boolean exists2 = lectureEnrollmentRepository.existsByLectureIdAndUserId(200L, 1L);
        boolean exists3 = lectureEnrollmentRepository.existsByLectureIdAndUserId(200L, 2L);

        // Then
        assertAll(
                () -> then(exists1).isTrue(),
                () -> then(exists2).isFalse(),
                () -> then(exists3).isTrue()
        );
    }
}