package com.lingotrainer.api.application.game;

import com.lingotrainer.api.domain.model.game.Game;

import java.util.Optional;

public interface GameService {
    Optional<Game> findById(int id);

    int createNewGame(Game game);

    int save(Game game);
}
