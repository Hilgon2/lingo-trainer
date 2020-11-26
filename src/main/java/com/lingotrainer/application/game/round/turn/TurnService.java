package com.lingotrainer.application.game.round.turn;

import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.Optional;

public interface TurnService {
    Turn findCurrentTurn(int roundId);

    Turn playTurn(int roundId, String guessedWord);

    Turn findById(int turnId);
}
