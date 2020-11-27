package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityMapperAutowire {

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public TurnMapper turnMapper() {
        return new TurnMapper();
    }

    @Bean
    public RoundMapper roundMapper() {
        return new RoundMapper();
    }

    @Bean
    public GameMapper gameMapper() {
        return new GameMapper();
    }
}