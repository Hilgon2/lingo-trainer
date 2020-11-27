package com.lingotrainer.api.web.mapper;

import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.CreateUserRequest;

public class UserFormMapper {
    public User convertToDomainEntity(CreateUserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .password(userRequest.getPassword())
                .build();
    }
}
