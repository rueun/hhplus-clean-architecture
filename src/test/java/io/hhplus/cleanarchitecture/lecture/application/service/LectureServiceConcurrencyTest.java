package io.hhplus.cleanarchitecture.lecture.application.service;

import io.hhplus.cleanarchitecture.common.time.FakeTimeProvider;
import io.hhplus.cleanarchitecture.common.time.TimeProvider;
import io.hhplus.cleanarchitecture.lecture.application.dto.command.EnrollLectureCommand;
import io.hhplus.cleanarchitecture.lecture.domain.entity.Lecture;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureEnrollment;
import io.hhplus.cleanarchitecture.lecture.domain.entity.LectureItem;
import io.hhplus.cleanarchitecture.lecture.domain.exception.LectureAlreadyEnrolledException;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureEnrollmentRepository;
import io.hhplus.cleanarchitecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
                .title("강의명")
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


    @Test
    void 다른_사용자_20명이_동시에_동일한_강의에_요청하는_경우_20명_모두_수강신청에_성공한다() throws InterruptedException {
        // Given
        Lecture lecture = Lecture.builder()
                .title("강의명")
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
        final int threadCount = 20;
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
                () -> then(failedCount.get()).isEqualTo(0),
                () -> then(resultLectureItem.getRemainingCapacity()).isEqualTo(10),
                () -> then(enrollmentsByLectureItemId.size()).isEqualTo(20)
        );
    }

    @Test
    void 동일한_사용자가_동일한_강의에_대해_5번_요청을_하면_1번만_수강신청에_성공한다() throws InterruptedException {
        // Given
        Lecture lecture = Lecture.builder()
                .title("강의명")
                .instructor("강사")
                .build();
        final Lecture savedLecture = lectureRepository.save(lecture);


        final LectureItem savedLectureItem = lectureRepository.saveItem(
                LectureItem.builder()
                        .capacity(30)
                        .remainingCapacity(30)
                        .lectureId(savedLecture.getId())
                        .lectureTime(LocalDateTime.parse("2024-10-05T10:00"))
                        .build()
        );

        // When
        final int threadCount = 5;
        AtomicInteger failedCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            executorService.execute(() -> {
                try {
                    lectureService.enroll(EnrollLectureCommand.builder()
                            .lectureId(savedLecture.getId())
                            .lectureItemId(savedLectureItem.getId())
                            .userId(1L)
                            .build());
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                    System.out.println(e.getClass() + " : " + e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        final List<LectureEnrollment> lectureEnrollments = lectureEnrollmentRepository.findAllByUserId(1L);
        final LectureItem lectureItem = lectureRepository.getItemById(lectureEnrollments.get(0).getLectureId(),
                lectureEnrollments.get(0).getLectureItemId());

        assertAll(
                () -> then(failedCount.get()).isEqualTo(4),
                () -> then(lectureEnrollments.size()).isEqualTo(1),
                () -> then(lectureItem.getRemainingCapacity()).isEqualTo(29)
        );
    }


    @Test
    void 동일한_사용자가_동일한_강의에_대해_10번_요청을_하면_1번만_수강신청에_성공한다() throws InterruptedException {
        // Given
        Lecture lecture = Lecture.builder()
                .title("강의명")
                .instructor("강사")
                .build();
        final Lecture savedLecture = lectureRepository.save(lecture);

        // 동일한 강의의 두 개의 LectureItem을 생성한다.
        LectureItem lectureItem1 = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(30)
                .lectureId(savedLecture.getId())
                .lectureTime(LocalDateTime.parse("2024-10-05T10:00"))
                .build();

        LectureItem lectureItem2 = LectureItem.builder()
                .capacity(30)
                .remainingCapacity(30)
                .lectureId(savedLecture.getId())
                .lectureTime(LocalDateTime.parse("2024-10-06T10:00"))
                .build();

        final LectureItem savedLectureItem1 = lectureRepository.saveAllItem(List.of(lectureItem1, lectureItem2)).get(0);
        final LectureItem savedLectureItem2 = lectureRepository.saveAllItem(List.of(lectureItem1, lectureItem2)).get(1);

        // When
        final int threadCount = 10;
        AtomicInteger failedCount = new AtomicInteger(0);
        List<Class<?>> errorClasses = new ArrayList<>(); // 에러 클래스 타입을 저장할 리스트
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            long finalI = i;
            executorService.execute(() -> {
                try {
                    lectureService.enroll(EnrollLectureCommand.builder()
                            .lectureId(savedLecture.getId())
                            .lectureItemId(finalI % 2 == 0 ? savedLectureItem2.getId() : savedLectureItem1.getId())
                            .userId(1L)
                            .build());
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                    errorClasses.add(e.getClass());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        final List<LectureEnrollment> lectureEnrollments = lectureEnrollmentRepository.findAllByUserId(1L);
        final LectureItem lectureItem = lectureRepository.getItemById(lectureEnrollments.get(0).getLectureId(),
                lectureEnrollments.get(0).getLectureItemId());

        assertAll(
                () -> then(failedCount.get()).isEqualTo(9),
                () -> then(lectureEnrollments.size()).isEqualTo(1),
                () -> then(lectureItem.getRemainingCapacity()).isEqualTo(29),
                () -> then(errorClasses).containsOnly(LectureAlreadyEnrolledException.class)
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