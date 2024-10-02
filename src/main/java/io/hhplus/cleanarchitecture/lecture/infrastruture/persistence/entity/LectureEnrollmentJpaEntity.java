package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "lecture_enrollment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lecture_id", "user_id"})
})
public class LectureEnrollmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비정규화로 인한 lecture_id 필드 추가
    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @JoinColumn(name = "lecture_item_id", nullable = false)
    private Long lectureItemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;
}
