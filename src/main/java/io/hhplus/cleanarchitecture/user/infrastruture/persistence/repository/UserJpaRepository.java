package io.hhplus.cleanarchitecture.user.infrastruture.persistence.repository;

import io.hhplus.cleanarchitecture.user.infrastruture.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
}
