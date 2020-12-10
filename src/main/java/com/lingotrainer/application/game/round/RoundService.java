package com.lingotrainer.application.game.round;

import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.List;

public interface RoundService {
    Round saveRound(Round round);

    Round findCurrentRound(int gameId);

    Round createNewRound(int gameId);

    Round findRoundById(int roundId);

    Turn findCurrentTurn(int roundId);

    Turn playTurn(int gameId, String guessedWord);

    Turn findTurnById(int turnId);

    List<Turn> findActiveTurnsByRoundId(int roundId);

    Turn saveTurn(Turn turn);
}
