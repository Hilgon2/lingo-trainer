package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;

public interface GameService {
    Game findById(int id);

    Game createNewGame(String languageCode);
    Game findActiveGame(int userId);
    boolean hasActiveGame(int userId);

    Game save(Game game);
}
