package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.user.User;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(int id);
    boolean hasActiveGame(@Param("user") User user);
    int save(Game game);
}
