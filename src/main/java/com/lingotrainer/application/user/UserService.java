package com.lingotrainer.application.user;

import com.lingotrainer.domain.model.user.User;

public interface UserService {
    User save(User user);

    boolean existsByUsername(String username);
}
