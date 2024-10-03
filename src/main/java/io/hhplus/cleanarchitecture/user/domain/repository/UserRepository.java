package io.hhplus.cleanarchitecture.user.domain.repository;

import io.hhplus.cleanarchitecture.user.domain.model.entity.User;

public interface UserRepository {
    User save(User user);
    User getById(Long id);
}
