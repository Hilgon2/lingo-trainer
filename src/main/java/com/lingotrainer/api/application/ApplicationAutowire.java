package com.lingotrainer.api.application;

import com.lingotrainer.api.util.mapper.GameMapper;
import com.lingotrainer.api.util.mapper.RoundMapper;
import com.lingotrainer.api.util.mapper.TurnMapper;
import com.lingotrainer.api.util.mapper.UserMapper;
import com.lingotrainer.api.domain.repository.*;
import com.lingotrainer.api.infrastructure.persistency.file.dictionary.base.BaseDictionaryFileRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.GameJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.RoundJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseGameJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseRoundJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseTurnJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseUserJpaRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationAutowire {

    private final ApplicationContext applicationContext;

    public ApplicationAutowire(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DictionaryRepository dictionaryRepository() {
        return new BaseDictionaryFileRepository();
    }

    @Bean
    public GameRepository gameRepository() {
        return new BaseGameJpaRepository((GameJpaRepository) applicationContext.getBean("gameJpaRepository"), (GameMapper) applicationContext.getBean("gameMapper"));
    }

    @Bean
    public RoundRepository roundRepository() {
        return new BaseRoundJpaRepository((RoundJpaRepository) applicationContext.getBean("roundJpaRepository"), (RoundMapper) applicationContext.getBean("roundMapper"));
    }

    @Bean
    public TurnRepository turnRepository() {
        return new BaseTurnJpaRepository((TurnJpaRepository) applicationContext.getBean("turnJpaRepository"), (TurnMapper) applicationContext.getBean("turnMapper"));
    }

    @Bean
    public UserRepository userRepository() {
        return new BaseUserJpaRepository((UserJpaRepository) applicationContext.getBean("userJpaRepository"), (UserMapper) applicationContext.getBean("userMapper"));
    }
}