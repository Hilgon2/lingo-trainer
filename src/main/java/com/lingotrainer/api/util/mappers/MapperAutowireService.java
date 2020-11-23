package com.lingotrainer.api.util.mappers;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Configuration
public class MapperAutowireService {

    private final ApplicationContext applicationContext;

    public MapperAutowireService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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