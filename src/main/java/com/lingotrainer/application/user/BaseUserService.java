package com.lingotrainer.application.user;

import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.repository.UserRepository;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.ForbiddenException;
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

    /**
     * Create or update a user, depending on if the user already exists or not.
     * @param user the user to be saved
     * @return user information of the saved user
     */
    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException(String.format("%s bestaat al", user.getUsername()));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // only an admin can create another admin
        if (user.getRole() == Role.ADMIN && (authenticationService.getUser() == null || authenticationService.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }

        return this.userRepository.save(user);
    }

    /**
     * Check if a username already exists or not.
     * @param username username to be checked
     * @return true or false
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format("User with username %s could not be found", username)));
    }
}
