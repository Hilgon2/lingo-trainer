package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;

import java.util.Optional;

public interface GameService {
    Optional<Game> findById(int id);

    int createNewGame(String languageCode);
    Optional<Game> findActiveGame(int userId);

    int save(Game game);
}
