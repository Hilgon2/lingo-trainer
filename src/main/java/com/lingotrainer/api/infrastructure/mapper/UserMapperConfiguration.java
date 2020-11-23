package com.lingotrainer.api.infrastructure.mapper;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.config.MapperConfiguration;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.modelmapper.ModelMapper;

public class UserMapperConfiguration implements MapperConfiguration {
    @Override
    public void execute(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserEntity.class, User.class)
        .addMapping(UserEntity::getUsername, User::setUsername)
        .addMapping(UserEntity::getPassword, User::setPassword)
        .addMapping(UserEntity::getRole, User::setRole);

        modelMapper.createTypeMap(User.class, UserEntity.class)
                .addMapping(User::getUsername, UserEntity::setUsername)
                .addMapping(User::getPassword, UserEntity::setPassword)
                .addMapping(User::getRole, UserEntity::setRole);
    }
}
