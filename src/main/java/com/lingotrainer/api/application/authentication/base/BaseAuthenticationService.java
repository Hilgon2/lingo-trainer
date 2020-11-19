package com.lingotrainer.api.application.authentication.base;

import com.lingotrainer.api.application.authentication.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import com.lingotrainer.api.domain.model.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseAuthenticationService implements AuthenticationService {
    private final ModelMapper modelMapper;

    public BaseAuthenticationService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = this.modelMapper.map(authentication.getPrincipal(), User.class);
            return user;
        }

        return null;
    }
}
