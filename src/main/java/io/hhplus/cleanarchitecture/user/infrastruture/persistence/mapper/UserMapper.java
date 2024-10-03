package io.hhplus.cleanarchitecture.user.infrastruture.persistence.mapper;

import io.hhplus.cleanarchitecture.user.domain.model.entity.User;
import io.hhplus.cleanarchitecture.user.infrastruture.persistence.entity.UserJpaEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserJpaEntity toJpaEntity(final User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toDomain(final UserJpaEntity userJpaEntity) {
        return User.builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .email(userJpaEntity.getEmail())
                .build();
    }
}
