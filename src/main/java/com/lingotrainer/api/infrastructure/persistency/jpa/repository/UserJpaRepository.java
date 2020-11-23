package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
