package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.util.mapper.GameMapper;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.GameJpaRepository;
import com.lingotrainer.api.util.exception.NotFoundException;

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
        return Optional.ofNullable(this.gameMapper.convertToDomainEntity(this.gameJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Game ID %d could not be found", id)))));
    }

    public boolean hasActiveGame(int userId) {
        return gameJpaRepository.hasActiveGame(userId);
    }

    public Optional<Game> findActiveGame(int userId) {
        return Optional.ofNullable(this.gameMapper.convertToDomainEntity(gameJpaRepository.findActiveGame(userId)));
    }

    public int save(Game game) {
        return this.gameJpaRepository.save(this.gameMapper.convertToPersistableEntity(game)).getId();
    }
}
