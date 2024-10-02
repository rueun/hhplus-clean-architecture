package io.hhplus.cleanarchitecture.lecture.infrastruture.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "lecture_item")
public class LectureItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "remaining_capacity", nullable = false)
    private int remainingCapacity;

    @Column(name = "lecture_time", nullable = false)
    private LocalDateTime lectureTime;
}


