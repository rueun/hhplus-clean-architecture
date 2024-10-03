package io.hhplus.cleanarchitecture.user.domain.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}
