package com.lingotrainer.domain;

import com.lingotrainer.domain.model.dictionary.LingoWordFilter;
import com.lingotrainer.domain.model.dictionary.WordFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainAutowire {

    @Bean
    public WordFilter wordFilter() {
        return new LingoWordFilter();
    }
}