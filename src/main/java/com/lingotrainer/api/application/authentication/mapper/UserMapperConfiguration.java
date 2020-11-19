package com.lingotrainer.api.application.authentication.mapper;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.config.MapperConfiguration;
import org.modelmapper.ModelMapper;
import com.lingotrainer.api.infrastructure.web.request.CreateUserRequest;

public class UserMapperConfiguration implements MapperConfiguration {
    @Override
    public void execute(ModelMapper modelMapper) {
        modelMapper.createTypeMap(CreateUserRequest.class, User.class)
        .addMapping(CreateUserRequest::getUsername, User::setUsername)
        .addMapping(CreateUserRequest::getPassword, User::setPassword)
        .addMapping(CreateUserRequest::getRole, User::setRole);
    }
}
