package com.lingotrainer.api.game.turn;

import com.lingotrainer.api.game.Game;
import com.lingotrainer.api.game.GameFeedback;
import com.lingotrainer.api.game.round.Round;
import com.lingotrainer.api.game.GameService;
import com.lingotrainer.api.game.round.RoundService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TurnServiceImpl implements TurnService {

    private TurnRepository turnRepository;

    public TurnServiceImpl(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    @Override
    public Turn save(Turn turn) {
        return turnRepository.save(turn);
    }

    @Override
    public Optional<Turn> findCurrentTurn(Round round) {
        return turnRepository.findCurrentTurn(round.getId());
    }
}
