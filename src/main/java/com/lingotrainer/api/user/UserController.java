package com.lingotrainer.api.user;

import com.lingotrainer.api.shared.annotation.Public;
import com.lingotrainer.api.shared.exception.DuplicateException;
import com.lingotrainer.api.shared.exception.ForbiddenException;
import com.lingotrainer.api.user.User;
import com.lingotrainer.api.security.Role;
import com.lingotrainer.api.authentication.AuthenticationService;
import com.lingotrainer.api.user.UserService;
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

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @Public
    public ResponseEntity<User> save(@Param("user") @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new DuplicateException(user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        if (user.getRole() == Role.ADMIN && (authenticationService.getUser() == null || authenticationService.getUser().getRole() != Role.ADMIN)) {
            throw new ForbiddenException("Only administrators are permitted to create another administrator account");
        }
        return ResponseEntity.ok(userService.save(user));
    }

}
