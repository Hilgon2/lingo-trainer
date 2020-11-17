package com.lingotrainer.api.game.round;

import com.lingotrainer.api.shared.exception.ForbiddenException;
import com.lingotrainer.api.shared.exception.NotFoundException;
import com.lingotrainer.api.dictionary.Dictionary;
import com.lingotrainer.api.game.Game;
import com.lingotrainer.api.game.turn.Turn;
import com.lingotrainer.api.game.GameRepository;
import com.lingotrainer.api.authentication.AuthenticationService;
import com.lingotrainer.api.game.turn.TurnService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class RoundServiceImpl implements RoundService {

    private final AuthenticationService authenticationService;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;

    public RoundServiceImpl(AuthenticationService authenticationService, RoundRepository roundRepository, GameRepository gameRepository) {
        this.authenticationService = authenticationService;
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

        if (round.getGame().getUser().getId() != authenticationService.getUser().getId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return roundRepository.save(round);
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        return roundRepository.findCurrentRound(gameId);
    }
}
