package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);
}
