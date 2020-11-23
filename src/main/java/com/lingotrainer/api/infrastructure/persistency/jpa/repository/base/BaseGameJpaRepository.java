package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.GameJpaRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class BaseGameJpaRepository implements GameRepository {
    private GameJpaRepository gameJpaRepository;
    private final ModelMapper modelMapper;

    public BaseGameJpaRepository(GameJpaRepository gameJpaRepository, ModelMapper modelMapper) {
        this.gameJpaRepository = gameJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Game> findById(int id) {
        return Optional.ofNullable(this.modelMapper.map(this.gameJpaRepository.findById(id), Game.class));
    }

    public boolean hasActiveGame(User user) {
        return gameJpaRepository.hasActiveGame(user);
    }

    public int save(Game game) {
        return this.gameJpaRepository.save(this.modelMapper.map(game, GameEntity.class)).getId();
    }
}
