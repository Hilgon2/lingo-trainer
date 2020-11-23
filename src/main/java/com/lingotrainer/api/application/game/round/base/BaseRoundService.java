package com.lingotrainer.api.application.game.round.base;

import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.domain.model.game.GameFeedback;
import com.lingotrainer.api.domain.model.game.GameId;
import com.lingotrainer.api.domain.model.game.GameStatus;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.game.round.RoundId;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.domain.repository.RoundRepository;
import com.lingotrainer.api.domain.repository.TurnRepository;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BaseRoundService implements RoundService {

    private final AuthenticationService authenticationService;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private TurnRepository turnRepository;

    public BaseRoundService(AuthenticationService authenticationService, RoundRepository roundRepository, GameRepository gameRepository, TurnRepository turnRepository) {
        this.authenticationService = authenticationService;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
        this.turnRepository = turnRepository;
    }

    @Override
    public Round save(Round round) {
        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));
        if (!gameRepository.hasActiveGame(game.getUserId())) {
            throw new NotFoundException("User has no active games");
        }
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return roundRepository.save(round);
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        return roundRepository.findCurrentRound(gameId);
    }

    @Override
    public void createNewRound(Game game) {
        Dictionary dictionary = Dictionary.builder()
                .language(game.getLanguage())
                .build();
        String randomWord = dictionary.getRandomWord();

        if (randomWord.length() < 5 || randomWord.length() > 7) {
            randomWord = dictionary.getRandomWord();
        }

        Round round = Round.builder()
                .word(randomWord)
                .gameId(new GameId(game.getGameId()))
                .active(true)
                .build();

        round = this.save(round);
        Turn newTurn = Turn.builder()
                .startedAt(Instant.now())
                .build();
        round.addTurnId(newTurn.getTurnId());
        newTurn.setRoundId(new RoundId(round.getRoundId()));
        this.roundRepository.save(round);
        this.turnRepository.save(newTurn);
    }

    @Override
    public Optional<Turn> findCurrentTurn(Round round) {
        return turnRepository.findCurrentTurn(round.getRoundId());
    }

    @Override
    public void finishTurn(Turn turn) {
        this.turnRepository.save(turn);
        Round round = this.roundRepository.findById(turn.getRoundId()).orElseThrow(() -> new NotFoundException(String.format("Round ID %d not found", turn.getRoundId())));

        if (!turn.isCorrectGuess() &&
                round.getTurnIds()
                        .stream()
                        .filter(t -> this.turnRepository.findById(t.getId()).orElseThrow(() -> new NotFoundException(String.format("Turn ID %d not found", t.getId()))).getGuessedWord() != null)
                        .count() == 5) {
            Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));
            Map<String, Object> feedback = new HashMap<>();

            game.setGameStatus(GameStatus.FINISHED);
            this.gameRepository.save(game);

            feedback.put("code", 5001);
            feedback.put("status", GameFeedback.GAME_OVER);
            turn.setFeedback(feedback);
        } else {
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();

            this.turnRepository.save(newTurn);
        }
    }
}
