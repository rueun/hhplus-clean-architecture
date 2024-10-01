package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "lecture")
public class LectureJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false)
    private String speaker;

    @Column(name = "max_enrollment_count", nullable = false)
    private int maxEnrollmentCount = 30;

    @Column(name = "enrolled_count", nullable = false)
    private int enrolledCount = 0;

    @Column(name = "enroll_open_at", nullable = false)
    private LocalDateTime enrollOpenAt;
}


