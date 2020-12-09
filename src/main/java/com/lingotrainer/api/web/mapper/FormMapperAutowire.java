package com.lingotrainer.api.web.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormMapperAutowire {

    @Bean
    public UserFormMapper userFormMapper() {
        return new UserFormMapper();
    }

    @Bean
    public RoundFormMapper roundFormMapper() {
        return new RoundFormMapper();
    }

    @Bean
    public DictionaryFormMapper dictionaryFormMapper() {
        return new DictionaryFormMapper();
    }

    @Bean
    public GameFormMapper gameFormMapper() {
        return new GameFormMapper();
    }

    @Bean
    public LoginFormMapper loginFormMapper() {
        return new LoginFormMapper();
    }

    @Bean
    public TurnFormMapper turnFormMapper() {
        return new TurnFormMapper();
    }
}