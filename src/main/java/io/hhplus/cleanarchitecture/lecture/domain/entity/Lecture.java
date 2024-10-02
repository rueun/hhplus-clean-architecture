package io.hhplus.cleanarchitecture.lecture.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Lecture {
    private Long id;
    private String title;
    private String speaker;
}
