package com.lingotrainer.application.game.round;

import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.domain.repository.TurnRepository;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.application.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class BaseRoundService implements RoundService {

    private final AuthenticationService authenticationService;
    private TurnRepository turnRepository;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private DictionaryRepository dictionaryRepository;

    public BaseRoundService(AuthenticationService authenticationService, TurnRepository turnRepository, RoundRepository roundRepository, GameRepository gameRepository, DictionaryRepository dictionaryRepository) {
        this.authenticationService = authenticationService;
        this.turnRepository = turnRepository;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public int save(Round round) {
        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));
        if (!gameRepository.hasActiveGame(game.getUserId())) {
            throw new NotFoundException("User has no active games");
        }
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return roundRepository.save(round).getRoundId();
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        return roundRepository.findCurrentRound(gameId);
    }

    @Override
    public Round createNewRound(int gameId) {
        if (!this.gameRepository.hasActiveGame(this.authenticationService.getUser().getUserId())) {
            throw new NotFoundException("No active games could be found");
        }

        Round currentRound = this.roundRepository.findCurrentRound(gameId).orElse(null);

        // if there is an active round, check if there are still turns left. If not, set the current round on inactive.
        if (currentRound != null) {
            if (currentRound.getTurnIds()
                    .stream()
                    .filter(turnId -> this.turnRepository.findById(turnId.getId()).orElseThrow(() -> new NotFoundException(String.format("Turn ID %d could not be found", turnId.getId()))).getGuessedWord() != null)
                    .count() < 5) {
                throw new GameException("There are still turns left on the current round. Please finish them before creating a new round.");
            }
            currentRound.setActive(false);
            this.roundRepository.save(currentRound);
        }

        // retrieve last round to retrieve the amount of letters the next word needs (5, 6 or 7)
        Round lastRound = this.roundRepository.findLastRound(gameId).orElse(null);
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", gameId)));
        Dictionary dictionary = this.dictionaryRepository.findByLanguage(game.getLanguage()).orElseThrow(() -> new NotFoundException(String.format("Dictionary language %s not found", game.getLanguage())));

        Round newRoundBuilder = Round.builder()
                .gameId(new GameId(gameId))
                .active(true)
                .build();
        newRoundBuilder.nextWord(lastRound, dictionary);

        Round newRound = this.roundRepository.save(newRoundBuilder);

        Turn newTurn = Turn.builder()
                .startedAt(Instant.now())
                .roundId(new RoundId(newRound.getRoundId()))
                .build();

        this.turnRepository.save(newTurn);

        return newRound;
    }

    @Override
    public Optional<Round> findById(int roundId) {
        return this.roundRepository.findById(roundId);
    }
}