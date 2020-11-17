package com.lingotrainer.api.game;

import java.util.Optional;

public interface GameService {
    Optional<Game> findById(int id);

    Game createNewGame(Game game);

    Game save(Game game);
}
