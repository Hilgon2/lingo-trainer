package com.lingotrainer.application.game.round;

import com.lingotrainer.application.dictionary.DictionaryService;
import com.lingotrainer.application.game.GameService;
import com.lingotrainer.application.game.round.turn.TurnService;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BaseRoundService implements RoundService {

    private RoundRepository roundRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private GameService gameService;

    @Autowired
    private DictionaryService dictionaryService;

    public BaseRoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Create or update a round, depending on if the round already exists or not.
     *
     * @param round the round to be saved
     * @return ID of the saved round
     */
    @Override
    public Round save(Round round) {
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
            currentRound.checkActiveTurns(this.turnService.findActiveTurnsByRoundId(currentRound.getRoundId()));
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

        this.turnService.save(newTurn);

        return newRound;
    }

    /**
     * Retrieve the round information.
     *
     * @param roundId ID of the round
     * @return round information based on the given ID
     */
    @Override
    public Round findById(int roundId) {
        return this.roundRepository.findById(roundId).orElseThrow(() ->
                new NotFoundException(String.format("Round ID %d could not be found", roundId)));
    }
}
