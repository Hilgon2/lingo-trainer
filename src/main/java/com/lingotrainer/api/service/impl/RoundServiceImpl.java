package com.lingotrainer.api.service.impl;

import com.lingotrainer.api.exception.ForbiddenException;
import com.lingotrainer.api.exception.NotFoundException;
import com.lingotrainer.api.model.Dictionary;
import com.lingotrainer.api.model.Game;
import com.lingotrainer.api.model.Round;
import com.lingotrainer.api.model.Turn;
import com.lingotrainer.api.repository.GameRepository;
import com.lingotrainer.api.repository.RoundRepository;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.RoundService;
import com.lingotrainer.api.service.TurnService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class RoundServiceImpl implements RoundService {

    private final AuthenticationFacade authenticationFacade;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private TurnService turnService;

    public RoundServiceImpl(AuthenticationFacade authenticationFacade, RoundRepository roundRepository, GameRepository gameRepository, TurnService turnService) {
        this.authenticationFacade = authenticationFacade;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
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

        if (round.getGame().getUser().getId() != authenticationFacade.getUser().getId()) {
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
