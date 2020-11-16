package com.lingotrainer.api.service;

import com.lingotrainer.api.model.Game;
import com.lingotrainer.api.model.Round;

import java.util.Optional;

public interface RoundService {
    Optional<Round> findById(int id);

    Round save(Round round);

    Optional<Round> findCurrentRound(int gameId);
}
