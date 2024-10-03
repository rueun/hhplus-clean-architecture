package io.hhplus.cleanarchitecture.user.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.user.domain.model.entity.User;
import io.hhplus.cleanarchitecture.user.domain.repository.UserRepository;
import io.hhplus.cleanarchitecture.user.infrastruture.persistence.entity.UserJpaEntity;
import io.hhplus.cleanarchitecture.user.infrastruture.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(final User user) {
        final UserJpaEntity userJpaEntity = userJpaRepository.save(UserMapper.toJpaEntity(user));
        return UserMapper.toDomain(userJpaEntity);
    }

    @Override
    public User getById(final Long id) {
        return userJpaRepository.findById(id)
                .map(UserMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }
}
