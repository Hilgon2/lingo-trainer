package com.lingotrainer.api.service.impl;

import com.lingotrainer.api.model.Game;
import com.lingotrainer.api.model.GameFeedback;
import com.lingotrainer.api.model.Round;
import com.lingotrainer.api.model.Turn;
import com.lingotrainer.api.repository.TurnRepository;
import com.lingotrainer.api.service.GameService;
import com.lingotrainer.api.service.RoundService;
import com.lingotrainer.api.service.TurnService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class TurnServiceImpl implements TurnService {

    private TurnRepository turnRepository;
    private GameService gameService;
    private RoundService roundService;

    public TurnServiceImpl(TurnRepository turnRepository, GameService gameService, RoundService roundService) {
        this.turnRepository = turnRepository;
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @Override
    public Turn save(Turn turn) {
        return turnRepository.save(turn);
    }

    @Override
    public Optional<Turn> findCurrentTurn(Round round) {
        return turnRepository.findCurrentTurn(round.getId());
    }

    @Override
    public void finishTurn(Turn turn) {
        this.save(turn);

        if (turn.getRound().getTurns()
                .stream()
                .filter(t -> t.getGuessedWord() != null)
                .count() >= 5) {
            Game game = turn.getRound().getGame();
            Map<String, Object> feedback = new HashMap<>();

            game.setGameStatus(Game.GameStatus.FINISHED);
            gameService.save(game);

            feedback.put("code", 5001);
            feedback.put("status", GameFeedback.GAME_OVER);
            turn.setFeedback(feedback);
        }

        if (turn.getRound().getTurns().size() == 5) {
            this.roundService.createNewRound(turn.getRound().getGame());
        } else {
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .round(turn.getRound())
                    .build();

            this.save(newTurn);
        }
    }


}
