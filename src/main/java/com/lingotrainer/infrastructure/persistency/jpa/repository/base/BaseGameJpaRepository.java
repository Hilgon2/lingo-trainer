package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.EntityMapper;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.GameMapper;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.GameJpaRepository;

import java.util.Optional;

public class BaseGameJpaRepository implements GameRepository {
    private GameJpaRepository gameJpaRepository;
    private EntityMapper<Game, GameEntity> gameMapper;

    public BaseGameJpaRepository(GameJpaRepository gameJpaRepository, GameMapper gameMapper) {
        this.gameJpaRepository = gameJpaRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Optional<Game> findById(int id) {
        GameEntity gameEntity = this.gameJpaRepository.findById(id).orElse(null);
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

    public Game save(Game game) {
        return this.gameMapper.convertToDomainEntity(
                this.gameJpaRepository.save(this.gameMapper.convertToPersistableEntity(game))
        );
    }
}
