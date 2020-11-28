package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.web.mapper.UserFormMapper;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.CreateUserResponse;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.application.user.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;
    private UserFormMapper userFormMapper;

    public UserController(UserService userService, UserFormMapper userFormMapper) {
        this.userService = userService;
        this.userFormMapper = userFormMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> save(@Param("user") @RequestBody CreateUserRequest user) {
        User newUser = this.userFormMapper.convertToDomainEntity(user);
        newUser.setActive(true);
        return ResponseEntity.ok(this.userFormMapper.convertToResponse(userService.save(newUser)));
    }

}
