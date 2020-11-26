package com.lingotrainer.api.application.game.round.turn;

import com.lingotrainer.api.domain.model.game.round.turn.Turn;

import java.util.Optional;

public interface TurnService {
    Optional<Turn> findCurrentTurn(int roundId);

    Turn playTurn(int roundId, String guessedWord);

    Optional<Turn> findById(int turnId);
}
