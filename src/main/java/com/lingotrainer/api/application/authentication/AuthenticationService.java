package com.lingotrainer.api.application.authentication;

import com.lingotrainer.api.domain.model.user.User;
import org.springframework.security.core.Authentication;

/**
 * Facade for dealing with Spring authentication inside the controller
 */
public interface AuthenticationService {

    /**
     * Returns the Spring authentication object
     * @return Authentication
     */
    Authentication getAuthentication();

    /**
     * Returns the User object if possible.
     * Unauthenticated request return null.
     * @return User or null
     */
    User getUser();
}
