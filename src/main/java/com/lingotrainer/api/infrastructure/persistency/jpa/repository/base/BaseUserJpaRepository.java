package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import com.lingotrainer.api.util.exception.NotFoundException;
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
        UserEntity userEntity = this.userJpaRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format("Username %s not found", username)));
        return Optional.ofNullable(this.modelMapper.map(this.userJpaRepository.findByUsername(username), User.class));
    }

    @Override
    public Optional<User> findByPrincipal(Object user) {
        System.out.println(this.modelMapper.map(UserEntity.class, User.class));
        System.out.println(user);
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userJpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return this.modelMapper.map(this.userJpaRepository.save(this.modelMapper.map(user, UserEntity.class)), User.class);
    }
}
