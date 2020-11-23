package com.lingotrainer.api.infrastructure.security;

import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Username: %s not found", username)));
    }
}
