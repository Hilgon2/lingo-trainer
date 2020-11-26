package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.game.Game;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(int id);
    boolean hasActiveGame(@Param("userId") int userId);
    Optional<Game> findActiveGame(int userId);
    int save(Game game);
}
