package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.LoginResponse;

public class LoginFormMapper {
    public LoginResponse convertToResponse(LoginResponse login) {
        return LoginResponse.builder()
                .token(login.getToken())
                .build();
    }
}
