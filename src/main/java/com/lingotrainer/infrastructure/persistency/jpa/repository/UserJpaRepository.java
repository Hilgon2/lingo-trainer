package com.lingotrainer.infrastructure.persistency.jpa.repository;

import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);
}