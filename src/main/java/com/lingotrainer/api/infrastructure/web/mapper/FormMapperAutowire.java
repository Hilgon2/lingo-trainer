package com.lingotrainer.api.infrastructure.web.mapper;

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

}