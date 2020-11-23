package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.infrastructure.web.request.CreateUserRequest;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.application.user.UserService;
import com.lingotrainer.api.util.annotation.Public;
import com.lingotrainer.api.util.exception.DuplicateException;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.domain.model.user.Role;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserService userService;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Public
    public ResponseEntity<User> save(@Param("user") @RequestBody CreateUserRequest user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new DuplicateException(user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == Role.ADMIN && (authenticationService.getUser() == null || authenticationService.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }
        User newUser = modelMapper.map(user, User.class);
        return ResponseEntity.ok(userService.save(newUser));
    }

}
