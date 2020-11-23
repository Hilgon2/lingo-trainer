package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findByPrincipal(Object user);

    boolean existsByUsername(String username);

    User save(User user);
}
