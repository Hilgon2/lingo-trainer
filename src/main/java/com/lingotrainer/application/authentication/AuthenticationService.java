package com.lingotrainer.application.authentication;

import com.lingotrainer.api.web.response.LoginResponse;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    Authentication getAuthentication();

    User getUser();

    LoginResponse login(AuthenticationRequest data);
}
