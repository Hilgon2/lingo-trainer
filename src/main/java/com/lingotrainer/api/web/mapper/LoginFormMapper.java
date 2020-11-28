package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.LoginResponse;

import java.util.Map;

public class LoginFormMapper {
    public LoginResponse convertToResponse(Map<Object, Object> login) {
        return LoginResponse.builder()
                .token(login.get("token").toString())
                .build();
    }
}
