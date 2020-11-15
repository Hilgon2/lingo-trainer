package com.lingotrainer.api.service;

import com.lingotrainer.api.model.Game;

import java.util.Optional;

public interface GameService {
    Optional<Game> findById(int id);

    Game createNewGame(Game game);

    Game save(Game game);
}
