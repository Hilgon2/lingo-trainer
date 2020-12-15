package com.lingotrainer.application.user;

import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.repository.UserRepository;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public BaseUserService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User createNewUser(User user, User currentUser) {
        if (this.userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException(String.format("Gebruikersnaam %s is al in gebruik", user.getUsername()));
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // if role is not explicitly given, make default Trainee
        if (user.getRole() == null) {
            user.setRole(Role.TRAINEE);
        }

        // only an admin can create another admin
        if (user.getRole() == Role.ADMIN
                && (currentUser == null
                || currentUser.getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }
        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("User with username %s could not be found", username)));
    }

    @Override
    public User findById(int userId) {
        return this.userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User ID %d could not be found", userId)));
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("Username %s could not be found", username)));
    }
}
