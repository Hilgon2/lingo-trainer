package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.api.web.mapper.UserFormMapper;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.application.user.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;
    private UserFormMapper userFormMapper;

    public UserController(UserService userService,
                          AuthenticationService authenticationService,
                          UserFormMapper userFormMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userFormMapper = userFormMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Param("user") @RequestBody CreateUserRequest user) {
        User newUser = this.userFormMapper.convertToDomainEntity(user);
        return ok(this.userFormMapper.convertToResponse(
                userService.createNewUser(newUser, authenticationService.getUser())));
    }

    @GetMapping(path = "/me")
    @Authenticated
    public ResponseEntity<UserResponse> findLoggedInUserDetails() {
        return ok(this.userFormMapper.convertToResponse(this.authenticationService.getUser()));
    }

    @GetMapping(path = "/highscores")
    @Authenticated
    public ResponseEntity<List<UserResponse>> retrieveTopHighscores() {
        return ok(this.userFormMapper.convertToResponsesList(this.userService.retrieveTopHighscores()));
    }
}
