package com.lingotrainer.api.infrastructure.mapper;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.config.MapperConfiguration;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.modelmapper.ModelMapper;

public class GameMapperConfiguration implements MapperConfiguration {
    @Override
    public void execute(ModelMapper modelMapper) {
        modelMapper.createTypeMap(GameEntity.class, Game.class)
                .addMapping(GameEntity::getGameStatus, Game::setGameStatus)
                .addMapping(GameEntity::getLanguage, Game::setLanguage)
                .addMapping(GameEntity::getScore, Game::setScore)
                .addMapping(GameEntity::getUser, Game::setUser);
    }
}
