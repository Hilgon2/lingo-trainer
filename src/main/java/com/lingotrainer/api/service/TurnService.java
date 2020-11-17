package com.lingotrainer.api.service;

import com.lingotrainer.api.model.Round;
import com.lingotrainer.api.model.Turn;

import java.util.Optional;

public interface TurnService {
    Turn save(Turn turn);

    Optional<Turn> findCurrentTurn(Round round);

    void finishTurn(Turn turn);
}
