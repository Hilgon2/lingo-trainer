package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.game.round.Round;

import java.util.Optional;

public interface RoundRepository {
    Optional<Round> findCurrentRound(int gameId);
    Optional<Round> findLastRound(int gameId);

    Round save(Round round);
    Optional<Round> findById(int roundId);
}
