package com.lingotrainer.application.authentication;

import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import com.lingotrainer.api.web.response.LoginResponse;
import com.lingotrainer.application.user.UserService;
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

@Component
@Slf4j
public class BaseAuthenticationService implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    public BaseAuthenticationService(AuthenticationManager authenticationManager,
                                     JwtTokenProvider jwtTokenProvider,
                                     UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }

    @Override
    public LoginResponse login(AuthenticationRequest data) {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            User user = this.userService.findByUsername(username);

            // Create the token and maps all the roles to the Spring RoleAuthority so Spring can handle the roles
            String token = jwtTokenProvider.createToken(user);

            return LoginResponse
                    .builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Ongeldige gebruikersnaam en/of wachtwoord");
        }
    }
}
