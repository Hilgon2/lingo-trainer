package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);
}