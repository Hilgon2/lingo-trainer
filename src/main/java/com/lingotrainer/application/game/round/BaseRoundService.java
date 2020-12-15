package com.lingotrainer.application.game.round;

import com.lingotrainer.application.dictionary.DictionaryService;
import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.application.game.GameService;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameTurn;
import com.lingotrainer.domain.model.game.GameTurnFeedback;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.repository.TurnRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BaseRoundService implements RoundService {

    private RoundRepository roundRepository;
    private AuthenticationService authenticationService;
    private GameService gameService;
    private DictionaryService dictionaryService;
    private TurnRepository turnRepository;
    private UserService userService;

    public BaseRoundService(RoundRepository roundRepository,
                            AuthenticationService authenticationService,
                            GameService gameService,
                            DictionaryService dictionaryService,
                            TurnRepository turnRepository,
                            UserService userService) {
        this.roundRepository = roundRepository;
        this.authenticationService = authenticationService;
        this.gameService = gameService;
        this.dictionaryService = dictionaryService;
        this.turnRepository = turnRepository;
        this.userService = userService;
    }

    /**
     * Create or update a round, depending on if the round already exists or not.
     *
     * @param round the round to be saved
     * @return ID of the saved round
     */
    @Override
    public Round saveRound(Round round) {
        Game game = this.gameService.findById(round.getGameId());
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return roundRepository.save(round);
    }

    /**
     * Find the current active round of the game id.
     *
     * @param gameId ID of the game
     * @return round information by game ID
     */
    @Override
    public Round findCurrentRound(int gameId) {
        return roundRepository.findCurrentRound(gameId).orElse(null);
    }

    /**
     * Create a new round. There can only be one active round at a time.
     * There must also be an active game before starting a round.
     *
     * @param gameId the game ID to create the round
     * @return round information of the created round
     */
    @Override
    public Round createNewRound(int gameId) {
        if (!this.gameService.hasActiveGame(this.authenticationService.getUser().getUserId())) {
            throw new NotFoundException("No active games could be found");
        }

        // retrieve current round and check if it still has active turns
        Round currentRound = this.findCurrentRound(gameId);

        if (currentRound != null) {
            currentRound.checkActiveTurns(this.findActiveTurnsByRoundId(currentRound.getRoundId()));
        }

        // retrieve last round to retrieve the amount of letters the next word needs (5, 6 or 7)
        Round lastRound = this.roundRepository.findLastRound(gameId).orElse(null);
        Game game = this.gameService.findById(gameId);

        Round newRoundBuilder = Round.builder()
                .gameId(new GameId(gameId))
                .active(true)
                .build();
        newRoundBuilder.nextWordLength(lastRound);
        newRoundBuilder.setWord(
                this.dictionaryService.retrieveRandomWord(game.getLanguage(), newRoundBuilder.getWordLength())
        );

        Round newRound = this.roundRepository.save(newRoundBuilder);

        Turn newTurn = Turn.builder()
                .startedAt(Instant.now())
                .roundId(new RoundId(newRound.getRoundId()))
                .build();

        this.saveTurn(newTurn);

        return newRound;
    }

    /**
     * Retrieve the round information.
     *
     * @param roundId ID of the round
     * @return round information based on the given ID
     */
    @Override
    public Round findRoundById(int roundId) {
        return this.roundRepository.findById(roundId).orElseThrow(() ->
                new NotFoundException(String.format("Round ID %d could not be found", roundId)));
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
                new NotFoundException(String.format("Active turn of game ID %d not found", gameId)));
        Round round = this.findRoundById(turn.getRoundId());

        // get game and dictionary after active round check
        Game game = this.gameService.findById(round.getGameId());

        turn.setGuessedWord(guessedWord);
        turn.validate(round.getWord(),
                this.dictionaryService.existsByWord(game.getLanguage(), turn.getGuessedWord()));

        this.turnRepository.save(turn);

        return this.finishTurn(turn, game, round);
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
    public Turn saveTurn(Turn turn) {
        return this.turnRepository.save(turn);
    }

    /**
     * Finish a turn and do an operation based on the outcome of the turn.
     *
     * @param turn  turn of the guess
     * @param game  game of the guess
     * @param round round of the guess
     * @return return the turn with feedback
     */
    private Turn finishTurn(Turn turn, Game game, Round round) {
        GameTurn gameTurn = GameTurn.builder()
                .user(this.userService.findById(game.getUserId()))
                .game(game)
                .round(round)
                .turn(turn)
                .activeTurns(this.findActiveTurnsByRoundId(round.getRoundId()))
                .build();
        gameTurn.performTurn();
        GameTurnFeedback gameTurnFeedback = gameTurn.getGameTurnFeedback();

        this.gameService.save(gameTurn.getGame());
        this.saveRound(gameTurn.getRound());
        if (gameTurn.getUser().getHighscore() > this.userService.findById(game.getUserId()).getHighscore()) {
            this.userService.save(gameTurn.getUser());
        }

        if (gameTurnFeedback == GameTurnFeedback.WORD_WRONG_NEW_TURN) {
            this.turnRepository.save(gameTurn.getNewTurn());
        }

        return gameTurn.getTurn();
    }
}
