package com.lingotrainer.api.util.mappers;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.web.request.CreateUserRequest;

public interface UserEntityMapper {
    User convertFormToDomainEntity(CreateUserRequest userRequest);
}
