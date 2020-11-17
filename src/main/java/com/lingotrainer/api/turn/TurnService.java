package com.lingotrainer.api.turn;

import com.lingotrainer.api.round.Round;

import java.util.Optional;

public interface TurnService {
    Turn save(Turn turn);

    Optional<Turn> findCurrentTurn(Round round);

    void finishTurn(Turn turn);
}
