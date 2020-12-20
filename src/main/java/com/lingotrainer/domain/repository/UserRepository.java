package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int userId);

    boolean existsByUsername(String username);

    User save(User user);

    List<User> retrieveTopHighscores();
}
