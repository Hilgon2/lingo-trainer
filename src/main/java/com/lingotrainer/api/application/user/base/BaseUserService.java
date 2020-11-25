package com.lingotrainer.api.application.user.base;

import com.lingotrainer.api.application.authentication.AuthenticationService;
import com.lingotrainer.api.application.user.UserService;
import com.lingotrainer.api.domain.model.user.Role;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.util.exception.DuplicateException;
import com.lingotrainer.api.util.exception.ForbiddenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    public BaseUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException(String.format("%s bestaat al", user.getUsername()));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == Role.ADMIN && (authenticationService.getUser() == null || authenticationService.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }

        return this.userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}