package com.lingotrainer.api.domain.repository;


import com.lingotrainer.api.domain.model.game.round.turn.Turn;

import java.util.Optional;

public interface TurnRepository {
    Optional<Turn> findCurrentTurn(int roundId);

    Turn save (Turn turn);
    Optional<Turn> findById(int turnId);
}
