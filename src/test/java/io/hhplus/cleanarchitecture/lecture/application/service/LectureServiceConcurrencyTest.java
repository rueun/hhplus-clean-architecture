package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.FakeTimeProvider;
import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.command.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class LectureServiceConcurrencyTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureEnrollmentRepository lectureEnrollmentRepository;

    @Autowired
    private TimeProvider timeProvider;

    @Test
    void 다른_사용자_40명이_동시에_동일한_강의에_요청하는_경우_30명만_수강신청에_성공한다() throws InterruptedException {
        // Given
        Lecture lecture = Lecture.builder()
                .title("강의")
                .instructor("강사")
                .build();
        final Lecture savedLecture = lectureRepository.save(lecture);

        LectureItem lectureItem = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(30)
                .lectureId(savedLecture.getId())
                .lectureTime(LocalDateTime.parse("2024-10-05T10:00"))
                .build();
        final LectureItem savedLectureItem = lectureRepository.saveItem(lectureItem);

        // When
        final int threadCount = 40;
        AtomicInteger failedCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            long finalI = i;
            executorService.execute(() -> {
                try {
                    lectureService.enroll(EnrollLectureCommand.builder()
                            .lectureId(savedLecture.getId())
                            .lectureItemId(savedLectureItem.getId())
                            .userId(finalI)
                            .build());
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        final LectureItem resultLectureItem = lectureRepository.getItemById(savedLecture.getId(), savedLectureItem.getId());
        final List<LectureEnrollment> enrollmentsByLectureItemId = lectureEnrollmentRepository.findAllByLectureItemId(savedLectureItem.getId());

        assertAll(
                () -> then(failedCount.get()).isEqualTo(10),
                () -> then(resultLectureItem.getRemainingCapacity()).isEqualTo(0),
                () -> then(enrollmentsByLectureItemId.size()).isEqualTo(30)
        );
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public TimeProvider timeProvider() {
            return new FakeTimeProvider(LocalDateTime.parse("2024-10-01T10:00"));
        }
    }

}