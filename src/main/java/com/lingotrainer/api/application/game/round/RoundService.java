package com.lingotrainer.api.application.game.round;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;

import java.util.Optional;

public interface RoundService {
    Round save(Round round);

    Optional<Round> findCurrentRound(int gameId);

    void createNewRound(Game game);

    Optional<Turn> findCurrentTurn(Round round);

    void finishTurn(Turn turn);
}
