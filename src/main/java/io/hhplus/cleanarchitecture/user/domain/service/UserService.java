package io.hhplus.cleanarchitecture.user.domain.service;

import io.hhplus.cleanarchitecture.user.domain.model.entity.User;
import io.hhplus.cleanarchitecture.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getById(final Long id) {
        return userRepository.getById(id);
    }
}
