package com.lingotrainer.application.user;

import com.lingotrainer.domain.model.user.User;

import java.util.Optional;

public interface UserService {
    User save(User user);

    User findByUsername(String username);
}
