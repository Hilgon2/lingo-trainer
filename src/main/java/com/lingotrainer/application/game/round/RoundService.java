package com.lingotrainer.application.game.round;

import com.lingotrainer.domain.model.game.round.Round;

import java.util.Optional;

public interface RoundService {
    int save(Round round);

    Optional<Round> findCurrentRound(int gameId);

    Round createNewRound(int gameId);

    Optional<Round> findById(int roundId);
}
