package com.lingotrainer.api.application.user;

import com.lingotrainer.api.domain.model.user.User;

import java.util.Optional;

public interface UserService {
    User save(User user);

    boolean existsByUsername(String username);
}
