package com.lingotrainer.application.authentication;

import com.lingotrainer.api.web.response.LoginResponse;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    /**
     * Gets the authentication based on logged in user.
     *
     * @return Authentication object
     */
    Authentication getAuthentication();

    /**
     * Gets the user based on the logged in user.
     *
     * @return user principal
     */
    User getUser();

    /**
     * Login to the application.
     *
     * @param data the data needed to make an authentication request
     * @return the JWT token of the request
     */
    LoginResponse login(AuthenticationRequest data);
}
