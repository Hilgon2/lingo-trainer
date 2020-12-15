package com.lingotrainer.application.game.round;

import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.List;

public interface RoundService {
    /**
     * Create or update a round, depending on if the round already exists or not.
     *
     * @param round the round to be saved
     * @return ID of the saved round
     */
    Round saveRound(Round round);

    /**
     * Find the current active round of the game id.
     *
     * @param gameId ID of the game
     * @return round information by game ID
     */
    Round findCurrentRound(int gameId);

    /**
     * Create a new round. There can only be one active round at a time.
     * There must also be an active game before starting a round.
     *
     * @param gameId the game ID to create the round
     * @return round information of the created round
     */
    Round createNewRound(int gameId);

    /**
     * Retrieve the round information.
     *
     * @param roundId ID of the round
     * @return round information based on the given ID
     */
    Round findRoundById(int roundId);

    /**
     * Find the current turn based on round ID.
     *
     * @param roundId the round ID to look for
     * @return current turn information by round ID
     */
    Turn findCurrentTurn(int roundId);

    /**
     * Take a guess and play the turn. If the user has guessed the word wrong for 5 turns, the game ends.
     *
     * @param gameId      the round ID to play the turn
     * @param guessedWord the guessed word
     * @return turn information with given feedback on the guess
     */
    Turn playTurn(int gameId, String guessedWord);

    /**
     * Find all active turns based on round ID.
     *
     * @param roundId round ID to look for
     * @return a list of turns
     */
    List<Turn> findActiveTurnsByRoundId(int roundId);

    /**
     * Save the turn.
     *
     * @param turn turn to be saved
     * @return returns saved turn
     */
    Turn saveTurn(Turn turn);
}
