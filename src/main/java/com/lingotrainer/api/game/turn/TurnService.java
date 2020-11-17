package com.lingotrainer.api.game.turn;

import com.lingotrainer.api.game.round.Round;

import java.util.Optional;

public interface TurnService {
    Turn save(Turn turn);

    Optional<Turn> findCurrentTurn(Round round);
}
