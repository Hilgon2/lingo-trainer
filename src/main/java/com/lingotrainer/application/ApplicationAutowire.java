package com.lingotrainer.application;

import com.lingotrainer.domain.repository.*;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.GameMapper;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.RoundMapper;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.TurnMapper;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.UserMapper;
import com.lingotrainer.infrastructure.persistency.file.dictionary.BaseDictionaryFileRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.GameJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.RoundJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.UserJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.base.BaseGameJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.base.BaseRoundJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.base.BaseTurnJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.base.BaseUserJpaRepository;
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
        return new BaseGameJpaRepository((GameJpaRepository) applicationContext.getBean("gameJpaRepository"),
                (GameMapper) applicationContext.getBean("gameMapper"));
    }

    @Bean
    public RoundRepository roundRepository() {
        return new BaseRoundJpaRepository((RoundJpaRepository) applicationContext.getBean("roundJpaRepository"),
                (RoundMapper) applicationContext.getBean("roundMapper"));
    }

    @Bean
    public TurnRepository turnRepository() {
        return new BaseTurnJpaRepository((TurnJpaRepository) applicationContext.getBean("turnJpaRepository"),
                (TurnMapper) applicationContext.getBean("turnMapper"));
    }

    @Bean
    public UserRepository userRepository() {
        return new BaseUserJpaRepository((UserJpaRepository) applicationContext.getBean("userJpaRepository"),
                (UserMapper) applicationContext.getBean("userMapper"));
    }
}