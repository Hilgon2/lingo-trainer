package com.lingotrainer.api.round;

import com.lingotrainer.api.shared.exception.ForbiddenException;
import com.lingotrainer.api.shared.exception.NotFoundException;
import com.lingotrainer.api.dictionary.Dictionary;
import com.lingotrainer.api.game.Game;
import com.lingotrainer.api.turn.Turn;
import com.lingotrainer.api.game.GameRepository;
import com.lingotrainer.api.authentication.AuthenticationService;
import com.lingotrainer.api.turn.TurnService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class RoundServiceImpl implements RoundService {

    private final AuthenticationService authenticationService;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private TurnService turnService;

    public RoundServiceImpl(AuthenticationService authenticationService, RoundRepository roundRepository, GameRepository gameRepository, TurnService turnService) {
        this.authenticationService = authenticationService;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
        this.turnService = turnService;
    }

    @Override
    public Optional<Round> findById(int id) {
        return roundRepository.findById(id);
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
        Turn newTurn = round.getTurns().get(0);
        newTurn.setRound(round);
        this.turnService.save(newTurn);
    }
}
