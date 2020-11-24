package com.lingotrainer.api.application.game.round.base;

import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.domain.model.game.GameId;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.game.round.RoundId;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.domain.repository.RoundRepository;
import com.lingotrainer.api.domain.repository.TurnRepository;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class BaseRoundService implements RoundService {

    private final AuthenticationService authenticationService;
    private TurnRepository turnRepository;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;

    public BaseRoundService(AuthenticationService authenticationService, TurnRepository turnRepository, RoundRepository roundRepository, GameRepository gameRepository) {
        this.turnRepository = turnRepository;
        this.authenticationService = authenticationService;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
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

        return roundRepository.save(round);
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        return roundRepository.findCurrentRound(gameId);
    }

    @Override
    public int createNewRound(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", gameId)));

        Dictionary dictionary = Dictionary.builder()
                .language(game.getLanguage())
                .build();

        String randomWord = dictionary.getRandomWord();

        if (randomWord.length() < 5 || randomWord.length() > 7) {
            randomWord = dictionary.getRandomWord();
        }

        Round newRound = Round.builder()
                .word(randomWord)
                .gameId(new GameId(gameId))
                .active(true)
                .build();

        int roundId = this.roundRepository.save(newRound);

        Turn newTurn = Turn.builder()
                .startedAt(Instant.now())
                .roundId(new RoundId(roundId))
                .build();

        this.turnRepository.save(newTurn);

        return roundId;
    }

    @Override
    public Optional<Round> findById(int roundId) {
        return this.roundRepository.findById(roundId);
    }
}
