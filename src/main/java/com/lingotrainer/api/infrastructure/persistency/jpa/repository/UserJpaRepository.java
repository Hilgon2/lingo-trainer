package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
