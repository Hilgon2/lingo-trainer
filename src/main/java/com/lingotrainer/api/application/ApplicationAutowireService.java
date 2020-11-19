package com.lingotrainer.api.application;

import com.lingotrainer.api.domain.repository.*;
import com.lingotrainer.api.infrastructure.persistency.file.dictionary.DictionaryFileRepository;
import com.lingotrainer.api.infrastructure.persistency.file.dictionary.base.BaseDictionaryFileRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.GameJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.RoundJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.UserJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseGameJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseRoundJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseTurnJpaRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.base.BaseUserJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAutowireService {

    private final ApplicationContext applicationContext;

    public ApplicationAutowireService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public GameRepository gameRepository() {
        return new BaseGameJpaRepository((GameJpaRepository) applicationContext.getBean("gameJpaRepository"), (ModelMapper) applicationContext.getBean("modelMapper"));
    }

    @Bean
    public RoundRepository roundRepository() {
        return new BaseRoundJpaRepository((RoundJpaRepository) applicationContext.getBean("roundJpaRepository"), (ModelMapper) applicationContext.getBean("modelMapper"));
    }

    @Bean
    public TurnRepository turnRepository() {
        return new BaseTurnJpaRepository((TurnJpaRepository) applicationContext.getBean("turnJpaRepository"), (ModelMapper) applicationContext.getBean("modelMapper"));
    }

    @Bean
    public UserRepository userRepository() {
        return new BaseUserJpaRepository((UserJpaRepository) applicationContext.getBean("userJpaRepository"), (ModelMapper) applicationContext.getBean("modelMapper"));
    }
}
