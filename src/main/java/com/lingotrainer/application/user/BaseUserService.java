package com.lingotrainer.application.user;

import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.repository.UserRepository;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseUserService implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    public BaseUserService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create or update a user, depending on if the user already exists or not.
     * @param user the user to be saved
     * @return user information of the saved user
     */
    @Override
    public User save(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.TRAINEE);
        }

        // only an admin can create another admin
        if (user.getRole() == Role.ADMIN && (this.authenticationService.getUser() == null
                || this.authenticationService.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }

        return this.userRepository.save(user);
    }

    /**
     *  Get user information by username.
     * @param username username to look for
     * @return returns user information
     */

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("User with username %s could not be found", username)));
    }

    @Override
    public User findById(int id) {
        return this.userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User ID %d could not be found", id)));
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("Username %s could not be found", username)));
    }
}
