package com.lingotrainer.application.authentication;

import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthenticationService {
    Authentication getAuthentication();

    User getUser();

    Map<Object, Object> login(AuthenticationRequest data);
}
