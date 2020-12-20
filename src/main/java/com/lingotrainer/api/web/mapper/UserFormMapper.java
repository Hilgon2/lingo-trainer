package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.CreateUserRequest;

import java.util.ArrayList;
import java.util.List;

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

    public List<UserResponse> convertToResponsesList(List<User> users) {
        List<UserResponse> userResponses = new ArrayList<>();

        users.forEach(user -> {
                    boolean admin = user.getRole() == Role.ADMIN;
            userResponses.add(UserResponse.builder()
                            .username(user.getUsername())
                            .highscore(user.getHighscore())
                            .admin(admin)
                            .build());
                }
        );

        return userResponses;
    }
}
