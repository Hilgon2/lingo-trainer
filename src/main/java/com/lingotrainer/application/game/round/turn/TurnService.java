package com.lingotrainer.application.game.round.turn;

import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.List;

public interface TurnService {
    Turn findCurrentTurn(int roundId);

    Turn playTurn(int gameId, String guessedWord);

    Turn findById(int turnId);

    List<Turn> findActiveTurnsByRoundId(int roundId);

    Turn save(Turn turn);
}
