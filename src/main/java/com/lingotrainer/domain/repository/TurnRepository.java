package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.List;
import java.util.Optional;

public interface TurnRepository {
    Optional<Turn> findCurrentTurn(int gameId);

    Turn save(Turn turn);
    Optional<Turn> findById(int turnId);
    List<Turn> findActiveTurnsByRoundId(int roundId);
}
