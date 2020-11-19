package com.lingotrainer.api.application.game.round.base;

import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.domain.model.game.GameFeedback;
import com.lingotrainer.api.domain.model.game.round.Round;
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
import java.util.Collections;
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
        if (!gameRepository.hasActiveGame(round.getGame().getUser())) {
            throw new NotFoundException("User has no active games");
        }
        if (round.getGame().getUser().getId() != authenticationService.getUser().getId()) {
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
                .game(game)
                .turns(Collections.singletonList(Turn.builder()
                        .startedAt(Instant.now())
                        .build()))
                .build();

        round = this.save(round);
//        Turn newTurn = round.getTurns().get(0);
//        newTurn.setRound(round);
        this.roundRepository.save(round);
    }

    @Override
    public Optional<Turn> findCurrentTurn(Round round) {
        return turnRepository.findCurrentTurn(round.getId());
    }

    @Override
    public void finishTurn(Turn turn) {
        this.turnRepository.save(turn);

        if (!turn.isCorrectGuess() &&
                turn.getRound().getTurns()
                        .stream()
                        .filter(t -> t.getGuessedWord() != null)
                        .count() == 5) {
            Game game = turn.getRound().getGame();
            Map<String, Object> feedback = new HashMap<>();

            game.setGameStatus(Game.GameStatus.FINISHED);
            this.gameRepository.save(game);

            feedback.put("code", 5001);
            feedback.put("status", GameFeedback.GAME_OVER);
            turn.setFeedback(feedback);
        } else {
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .round(turn.getRound())
                    .build();

            this.turnRepository.save(newTurn);
        }
    }
}
