package com.lingotrainer.application.game.round.turn;

import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.dictionary.DictionaryService;
import com.lingotrainer.application.game.GameService;
import com.lingotrainer.application.game.round.RoundService;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameTurnFeedback;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.GameTurn;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import com.lingotrainer.domain.repository.*;
import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseTurnService implements TurnService {

    private TurnRepository turnRepository;

    @Autowired
    private RoundService roundService;

    @Autowired
    private GameService gameService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    public BaseTurnService(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    /**
     * Find the current turn based on round ID.
     *
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
     *
     * @param gameId      the round ID to play the turn
     * @param guessedWord the guessed word
     * @return turn information with given feedback on the guess
     */
    @Override
    public Turn playTurn(int gameId, String guessedWord) {
        Turn turn = this.turnRepository.findCurrentTurn(gameId).orElseThrow(() ->
                new NotFoundException(String.format("Active turn of round ID %d not found", gameId)));
        Round round = this.roundService.findById(turn.getRoundId());

        if (!round.isActive()) {
            throw new GameException(String.format("Round ID %d is not active. Please create a new round",
                    round.getRoundId()));
        }

        // get game and dictionary after active round check
        Game game = this.gameService.findById(round.getGameId());

        turn.setGuessedWord(guessedWord);
        turn.validate(round.getWord(),
                this.dictionaryService.existsByWord(game.getLanguage(), turn.getGuessedWord()));

        this.turnRepository.save(turn);

        return this.finishTurn(turn, game, round);
    }

    /**
     * Retrieve the turn information.
     *
     * @param turnId ID of the turn
     * @return turn information based on the given ID
     */
    @Override
    public Turn findById(int turnId) {
        return this.turnRepository.findById(turnId).orElseThrow(() ->
                new NotFoundException(String.format("Turn ID %d could not be found", turnId)));
    }

    /**
     * Find all active turns based on round ID.
     *
     * @param roundId round ID to look for
     * @return a list of turns
     */
    @Override
    public List<Turn> findActiveTurnsByRoundId(int roundId) {
        return this.turnRepository.findActiveTurnsByRoundId(roundId);
    }

    /**
     * Save the turn.
     *
     * @param turn turn to be saved
     * @return returns saved turn
     */
    @Override
    public Turn save(Turn turn) {
        return this.turnRepository.save(turn);
    }

    /**
     * Finish a turn and do an operation based on the outcome of the turn.
     *
     * @param turn turn of the guess
     * @param game game of the guess
     * @param round round of the guess
     * @return return the turn with feedback
     */
    private Turn finishTurn(Turn turn, Game game, Round round) {
        GameTurn gameTurn = GameTurn.builder()
                .user(this.userService.findById(game.getUserId()))
                .game(game)
                .round(round)
                .turn(turn)
                .activeTurns(
                        this.findActiveTurnsByRoundId(round.getRoundId())
                                .stream()
                                .map(tempTurn -> new TurnId(tempTurn.getTurnId()))
                                .collect(Collectors.toList())
                )
                .build();
        gameTurn.performTurn();
        GameTurnFeedback gameTurnFeedback = gameTurn.getGameTurnFeedback();

        if (gameTurnFeedback == GameTurnFeedback.WORD_CORRECT) {
            this.gameService.save(gameTurn.getGame());
            this.roundService.save(round);
            if (gameTurn.getUser().getHighscore() > this.userService.findById(game.getUserId()).getHighscore()) {
                this.userService.save(gameTurn.getUser());
            }
        } else if (gameTurnFeedback == GameTurnFeedback.WORD_WRONG_NO_TURNS_LEFT) {
            this.gameService.save(gameTurn.getGame());
            this.roundService.save(round);
        } else if (gameTurnFeedback == GameTurnFeedback.WORD_WRONG_NEW_TURN) {
            this.turnRepository.save(gameTurn.getNewTurn());
        }

        return turn;
    }
}
