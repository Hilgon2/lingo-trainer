package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.EntityMapper;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.UserMapper;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.repository.UserRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.UserJpaRepository;

import java.util.Optional;

public class BaseUserJpaRepository implements UserRepository {

    private UserJpaRepository userJpaRepository;
    private EntityMapper<User, UserEntity> userMapper;

    public BaseUserJpaRepository(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.userMapper.convertToDomainEntity(
                this.userJpaRepository.findByUsername(username))
        );
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userJpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return this.userMapper.convertToDomainEntity(
                this.userJpaRepository.save(this.userMapper.convertToPersistableEntity(user))
        );
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(this.userMapper.convertToDomainEntity(this.userJpaRepository.findById(userId).orElse(null)));
    }
}
