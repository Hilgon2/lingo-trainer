package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.game.round.Round;

import java.util.Optional;

public interface RoundRepository {
    Optional<Round> findCurrentRound(int gameId);

    Round save (Round round);
}
