package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserMapper implements EntityMapper<User, UserEntity> {

    @Override
    public User convertToDomainEntity(UserEntity userEntity) {
        return User.builder()
                .userId(new UserId(userEntity.getId()))
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .active(userEntity.isActive())
                .highscore(userEntity.getHighscore())
                .password(userEntity.getPassword())
                .build();
    }

    @Override
    public UserEntity convertToPersistableEntity(User user) {
        return UserEntity.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .active(user.isActive())
                .highscore(user.getHighscore())
                .password(user.getPassword())
                .build();
    }

    @Override
    public List<UserEntity> convertToPersistableEntities(List<User> users) {
        List<UserEntity> userEntities = new ArrayList<>();

        users.forEach(user -> userEntities.add(
                UserEntity.builder()
                        .id(user.getUserId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .active(user.isActive())
                        .highscore(user.getHighscore())
                        .password(user.getPassword())
                        .build()
        ));

        return userEntities;
    }

    @Override
    public List<User> convertToDomainEntities(List<UserEntity> userEntities) {
        List<User> users = new ArrayList<>();

        userEntities.forEach(user -> users.add(
                User.builder()
                        .userId(new UserId(user.getId()))
                        .username(user.getUsername())
                        .role(user.getRole())
                        .active(user.isActive())
                        .highscore(user.getHighscore())
                        .password(user.getPassword())
                        .build()
        ));

        return users;
    }
}
