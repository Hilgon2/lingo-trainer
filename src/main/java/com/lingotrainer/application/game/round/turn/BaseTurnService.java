package com.lingotrainer.application.game.round.turn;

import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.repository.*;
import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BaseTurnService implements TurnService {

    private TurnRepository turnRepository;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private DictionaryRepository dictionaryRepository;
    private UserRepository userRepository;

    public BaseTurnService(TurnRepository turnRepository,
                           RoundRepository roundRepository,
                           GameRepository gameRepository,
                           DictionaryRepository dictionaryRepository,
                           UserRepository userRepository) {
        this.turnRepository = turnRepository;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Find the current turn based on round ID.
     * @param roundId the round ID to look for
     * @return current turn information by round ID
     */
    @Override
    public Turn findCurrentTurn(int roundId) {
        return turnRepository.findCurrentTurn(roundId).orElseThrow(() ->
                new NotFoundException(String.format("Active turn with round ID %d could not be found", roundId)));
    }

    /**
     * Take a guess and play the turn. If the user has guessed the word wrong for 5 turns, the game ends.
     * @param gameId the round ID to play the turn
     * @param guessedWord the guessed word
     * @return turn information with given feedback on the guess
     */
    @Override
    public Turn playTurn(int gameId, String guessedWord) {
        Turn turn = this.turnRepository.findCurrentTurn(gameId).orElseThrow(() ->
                new NotFoundException(String.format("Active turn of round ID %d not found", gameId)));
        Round round = this.roundRepository.findById(turn.getRoundId()).orElseThrow(() ->
                new NotFoundException(String.format("Round ID %d not found", turn.getRoundId())));

        if (!round.isActive()) {
            throw new GameException(String.format("Round ID %d is not active. Please create a new round",
                    round.getRoundId()));
        }

        // get game and dictionary after active round check
        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() ->
                new NotFoundException(String.format("Game ID %d not found", round.getGameId())));

        turn.setGuessedWord(guessedWord);
        turn.validate(round.getWord(), this.dictionaryRepository.existsByWord(game.getLanguage(), turn.getGuessedWord()));

        this.turnRepository.save(turn);

        return this.finishTurn(turn, game, round);
    }

    /**
     * Retrieve the turn information.
     * @param turnId ID of the turn
     * @return turn information based on the given ID
     */
    @Override
    public Turn findById(int turnId) {
        return this.turnRepository.findById(turnId).orElseThrow(() ->
                new NotFoundException(String.format("Turn ID %d could not be found", turnId)));
    }

    private Turn finishTurn(Turn turn, Game game, Round round) {
        boolean roundActive = true;

        if (turn.isCorrectGuess()) {
            game.setScore(game.getScore() + 1);

            this.gameRepository.save(game);
            roundActive = false;
        } else if (!turn.isCorrectGuess()
                && round.getTurnIds()
                .stream()
                .filter(t -> this.turnRepository.findById(t.getId()).orElseThrow(() ->
                        new NotFoundException(String.format("Turn ID %d not found", t.getId())))
                        .getGuessedWord() != null)
                .count() >= 5) {

            game.setGameStatus(GameStatus.FINISHED);
            this.gameRepository.save(game);

            User user = this.userRepository.findById(game.getUserId()).orElseThrow(() ->
                    new NotFoundException(String.format("User ID %d not found", game.getUserId())));
            user.setHighscore(game.getScore());
            this.userRepository.save(user);

            turn.finishGame();
            roundActive = false;
        } else {
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();

            this.turnRepository.save(newTurn);
        }

        if (!roundActive) {
            round.setActive(false);
            this.roundRepository.save(round);
        }

        return turn;
    }
}
