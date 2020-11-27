package com.lingotrainer.application.authentication;

import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import com.lingotrainer.domain.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BaseAuthenticationService implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public BaseAuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Gets the authentication based on logged in user.
     *
     * @return Authentication object
     */
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Gets the user based on the logged in user.
     *
     * @return user principal
     */
    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * Login to the application.
     *
     * @param data the data needed to make an authentication request
     * @return the JWT token of the request
     */
    @Override
    public Map<Object, Object> login(AuthenticationRequest data) {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            // Create the token and maps all the roles to the Spring RoleAuthority so Spring can handle the roles
            String token = jwtTokenProvider.createToken(username);

            Map<Object, Object> model = new HashMap<>();
            model.put("token", token);
            return model;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }
}
