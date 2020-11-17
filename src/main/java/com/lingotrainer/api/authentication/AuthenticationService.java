package com.lingotrainer.api.authentication;

import com.lingotrainer.api.user.User;
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

    /**
     * Function to check if the user is an admin
     * @return true if user is admin
     */
    boolean isAdmin();
}
