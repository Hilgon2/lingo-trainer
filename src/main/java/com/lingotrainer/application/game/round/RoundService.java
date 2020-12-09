package com.lingotrainer.application.game.round;

import com.lingotrainer.domain.model.game.round.Round;

public interface RoundService {
    Round save(Round round);

    Round findCurrentRound(int gameId);

    Round createNewRound(int gameId);

    Round findById(int roundId);
}
