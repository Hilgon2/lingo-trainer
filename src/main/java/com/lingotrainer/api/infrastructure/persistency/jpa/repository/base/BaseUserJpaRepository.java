package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class BaseUserJpaRepository implements UserRepository {

    private UserJpaRepository userJpaRepository;
    private final ModelMapper modelMapper;

    public BaseUserJpaRepository(UserJpaRepository userJpaRepository, ModelMapper modelMapper) {
        this.userJpaRepository = userJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.modelMapper.map(this.userJpaRepository.findByUsername(username), User.class));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userJpaRepository.existsByUsername(username);
    }
}
