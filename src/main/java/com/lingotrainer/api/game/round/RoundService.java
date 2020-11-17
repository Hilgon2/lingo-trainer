package com.lingotrainer.api.game.round;

import com.lingotrainer.api.game.Game;

import java.util.Optional;

public interface RoundService {
    Optional<Round> findById(int id);

    Round save(Round round);

    Optional<Round> findCurrentRound(int gameId);

    void createNewRound(Game game);
}
