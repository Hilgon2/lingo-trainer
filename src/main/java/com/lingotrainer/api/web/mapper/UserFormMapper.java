package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.CreateUserRequest;

public class UserFormMapper {
    public User convertToDomainEntity(CreateUserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .password(userRequest.getPassword())
                .active(userRequest.isActive())
                .build();
    }

    public UserResponse convertToResponse(User user) {
        boolean admin = user.getRole() == Role.ADMIN;
        return UserResponse.builder()
                .username(user.getUsername())
                .highscore(user.getHighscore())
                .admin(admin)
                .build();
    }
}
