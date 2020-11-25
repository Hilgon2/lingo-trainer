package com.lingotrainer.api.infrastructure.web.mapper;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.web.request.CreateUserRequest;

public class UserFormMapper {
    public User convertToDomainEntity(CreateUserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .password(userRequest.getPassword())
                .build();
    }
}
