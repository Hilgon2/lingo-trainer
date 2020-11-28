package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;

import java.util.Optional;

public interface GameService {
    Optional<Game> findById(int id);

    Game createNewGame(String languageCode);
    Optional<Game> findActiveGame(int userId);

    Game save(Game game);
}
