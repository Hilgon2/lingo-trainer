package com.lingotrainer.api.application.user.base;

import com.lingotrainer.api.application.user.UserService;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService implements UserService {

    private UserRepository userRepository;

    public BaseUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
