package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.GameMapper;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.GameJpaRepository;
import com.lingotrainer.application.exception.NotFoundException;

import java.util.Optional;

public class BaseGameJpaRepository implements GameRepository {
    private GameJpaRepository gameJpaRepository;
    private GameMapper gameMapper;

    public BaseGameJpaRepository(GameJpaRepository gameJpaRepository, GameMapper gameMapper) {
        this.gameJpaRepository = gameJpaRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Optional<Game> findById(int id) {
        GameEntity gameEntity = this.gameJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Game ID %d could not be found", id)));
        if (gameEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.gameMapper.convertToDomainEntity(gameEntity));
    }

    public boolean hasActiveGame(int userId) {
        return gameJpaRepository.hasActiveGame(userId);
    }

    public Optional<Game> findActiveGame(int userId) {
        GameEntity gameEntity = gameJpaRepository.findActiveGame(userId);

        if (gameEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.gameMapper.convertToDomainEntity(gameEntity));
    }

    public int save(Game game) {
        return this.gameJpaRepository.save(this.gameMapper.convertToPersistableEntity(game)).getId();
    }
}
