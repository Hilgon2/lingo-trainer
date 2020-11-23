package com.lingotrainer.api.application.authentication.base;

import com.lingotrainer.api.application.authentication.AuthenticationService;
import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import com.lingotrainer.api.domain.model.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseAuthenticationService implements AuthenticationService {
    private UserRepository userRepository;

    public BaseAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return this.userRepository.findByPrincipal(authentication.getPrincipal()).orElseThrow(() -> new NotFoundException("Username not found"));
        }

        return null;
    }
}
