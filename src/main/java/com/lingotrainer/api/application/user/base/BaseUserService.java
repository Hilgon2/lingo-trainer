package com.lingotrainer.api.application.user.base;

import com.lingotrainer.api.application.user.UserService;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BaseUserService implements UserService {

    private UserJpaRepository userRepository;
    private ModelMapper modelMapper;

    public BaseUserService(UserJpaRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll().stream().map(userEntity -> this.modelMapper.map(userEntity, User.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(this.modelMapper.map(userRepository.findById(id), User.class));
    }

    @Override
    public User save(User user) {
        return this.modelMapper.map(userRepository.save(this.modelMapper.map(user, UserEntity.class)), User.class);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.modelMapper.map(userRepository.findByUsername(username), User.class));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
