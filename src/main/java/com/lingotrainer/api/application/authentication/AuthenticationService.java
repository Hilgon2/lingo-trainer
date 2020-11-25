package com.lingotrainer.api.application.authentication;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.web.request.AuthenticationRequest;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * Facade for dealing with Spring authentication inside the controller
 */
public interface AuthenticationService {
    Authentication getAuthentication();

    User getUser();

    Map<Object, Object> login(AuthenticationRequest data);
}
