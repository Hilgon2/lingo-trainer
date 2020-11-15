package com.lingotrainer.api.controller;

import com.lingotrainer.api.annotation.Public;
import com.lingotrainer.api.exception.DuplicateException;
import com.lingotrainer.api.exception.ForbiddenException;
import com.lingotrainer.api.model.User;
import com.lingotrainer.api.security.Role;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.UserService;
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
    private final AuthenticationFacade authenticationFacade;

    public UserController(UserService userService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping
    @Public
    public ResponseEntity<User> save(@Param("user") @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new DuplicateException(user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        if (user.getRole() == Role.ADMIN && (authenticationFacade.getUser() == null || authenticationFacade.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }
        return ResponseEntity.ok(userService.save(user));
    }

}
