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
    public Turn finishTurn(int turnId, String guessedWord) {
        Turn turn = this.turnRepository.findById(turnId).orElseThrow(() -> new NotFoundException(String.format("Turn ID % not found", turnId)));
        turn.setGuessedWord(guessedWord);

        turn.validateTurn(guessedWord);
        this.turnRepository.save(turn);

        Turn finalTurn = turn;
        Round round = this.roundRepository.findById(turn.getRoundId()).orElseThrow(() -> new NotFoundException(String.format("Round ID %d not found", finalTurn.getRoundId())));
        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));

        boolean correctGuess = turn.getGuessedWord().equalsIgnoreCase(guessedWord);

        if (correctGuess) {
            game.setScore(game.getScore() + 1);
            this.roundRepository.createNewRound(game.getGameId());
            this.gameRepository.save(game);
        } else if (!turn.isCorrectGuess() &&
                round.getTurnIds()
                        .stream()
                        .filter(t -> this.turnRepository.findById(t.getId()).orElseThrow(() -> new NotFoundException(String.format("Turn ID %d not found", t.getId()))).getGuessedWord() != null)
                        .count() == 5) {
            Map<String, Object> feedback = new HashMap<>();

            game.setGameStatus(GameStatus.FINISHED);
            this.gameRepository.save(game);

            feedback.put("code", 5001);
            feedback.put("status", GameFeedback.GAME_OVER);
            turn.setFeedback(feedback);
        } else {
            turn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();

            this.turnRepository.save(turn);
        }

        return turn;
    }

    @Override
    public Optional<Turn> findById(int turnId) {
        return this.turnRepository.findById(turnId);
    }
}
