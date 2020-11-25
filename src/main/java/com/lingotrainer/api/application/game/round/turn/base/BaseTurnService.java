package com.lingotrainer.api.application.game.round.turn.base;

import com.lingotrainer.api.application.game.round.turn.TurnService;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.GameFeedback;
import com.lingotrainer.api.domain.model.game.GameStatus;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.game.round.RoundId;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.domain.repository.RoundRepository;
import com.lingotrainer.api.domain.repository.TurnRepository;
import com.lingotrainer.api.util.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BaseTurnService implements TurnService {

    private TurnRepository turnRepository;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;

    public BaseTurnService(TurnRepository turnRepository, RoundRepository roundRepository, GameRepository gameRepository) {
        this.turnRepository = turnRepository;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Turn> findCurrentTurn(int roundId) {
        return turnRepository.findCurrentTurn(roundId);
    }

    @Override
    public Turn finishTurn(int roundId, String guessedWord) {
        Turn currentTurn = this.turnRepository.findCurrentTurn(roundId).orElseThrow(() -> new NotFoundException(String.format("Active turn of round ID % not found", roundId)));
        Round round = this.roundRepository.findById(currentTurn.getRoundId()).orElseThrow(() -> new NotFoundException(String.format("Round ID %d not found", currentTurn.getRoundId())));
        currentTurn.setGuessedWord(guessedWord);
        currentTurn.validateTurn(round.getWord());

        this.turnRepository.save(currentTurn);

        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));

        if (currentTurn.isCorrectGuess()) {
            game.setScore(game.getScore() + 1);
            this.gameRepository.save(game);
        } else if (!currentTurn.isCorrectGuess() &&
                round.getTurnIds()
                        .stream()
                        .filter(t -> this.turnRepository.findById(t.getId()).orElseThrow(() -> new NotFoundException(String.format("Turn ID %d not found", t.getId()))).getGuessedWord() != null)
                        .count() >= 5) {
            Map<String, Object> feedback = new HashMap<>();

            game.setGameStatus(GameStatus.FINISHED);
            this.gameRepository.save(game);

            feedback.put("code", 5001);
            feedback.put("status", GameFeedback.GAME_OVER);
            currentTurn.setFeedback(feedback);
        } else {
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();

            this.turnRepository.save(newTurn);
        }

        if (Integer.parseInt(currentTurn.getFeedback().get("code").toString()) == -9999) {
            currentTurn.setGuessedLetters(round.getWord());
        }

        return currentTurn;
    }

    @Override
    public Optional<Turn> findById(int turnId) {
        return this.turnRepository.findById(turnId);
    }
}