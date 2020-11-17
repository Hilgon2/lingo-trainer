package com.lingotrainer.api.authentication;

import lombok.extern.slf4j.Slf4j;
import com.lingotrainer.api.user.User;
import com.lingotrainer.api.security.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

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
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Role.ADMIN.getValue()));
    }
}
