package com.lingotrainer.api.service.impl;

import com.lingotrainer.api.exception.DuplicateException;
import com.lingotrainer.api.exception.ForbiddenException;
import com.lingotrainer.api.exception.NotFoundException;
import com.lingotrainer.api.model.Game;
import com.lingotrainer.api.repository.GameRepository;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.GameService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final AuthenticationFacade authenticationFacade;
    private GameRepository gameRepository;

    public GameServiceImpl(AuthenticationFacade authenticationFacade, GameRepository gameRepository) {
        this.authenticationFacade = authenticationFacade;
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Game> findById(int id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game createNewGame(Game game) {
        if (gameRepository.hasActiveGame(game.getUser())) {
            throw new DuplicateException("User already has an active game");
        }

        return gameRepository.save(game);
    }

    @Override
    public Game save(Game game) {
        if (!gameRepository.hasActiveGame(game.getUser())) {
            throw new NotFoundException("User has no active games");
        }

        if (game.getUser().getId() != authenticationFacade.getUser().getId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return gameRepository.save(game);
    }
}
