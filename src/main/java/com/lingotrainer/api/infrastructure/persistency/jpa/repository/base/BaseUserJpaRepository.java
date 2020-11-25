package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.util.mapper.UserMapper;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.UserRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;

import java.util.Optional;

public class BaseUserJpaRepository implements UserRepository {

    private UserJpaRepository userJpaRepository;
    private UserMapper userMapper;

    public BaseUserJpaRepository(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.userMapper.convertToDomainEntity(this.userJpaRepository.findByUsername(username)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userJpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return this.userMapper.convertToDomainEntity(this.userJpaRepository.save(this.userMapper.convertToPersistableEntity(user)));
    }
}
