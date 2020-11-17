package com.lingotrainer.api.service;

import com.lingotrainer.api.model.User;

import java.util.Optional;

public interface UserService {

    Iterable<User> findAll();

    Optional<User> findById(int id);

    User save(User user);

    void deleteUser(int id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}