package com.lingotrainer.api.game;

import com.lingotrainer.api.shared.exception.DuplicateException;
import com.lingotrainer.api.shared.exception.ForbiddenException;
import com.lingotrainer.api.shared.exception.NotFoundException;
import com.lingotrainer.api.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final AuthenticationService authenticationService;
    private GameRepository gameRepository;

    public GameServiceImpl(AuthenticationService authenticationService, GameRepository gameRepository) {
        this.authenticationService = authenticationService;
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Game> findById(int id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game createNewGame(Game game) {
        if (gameRepository.hasActiveGame(game.getUser())) {
            throw new DuplicateException("An active game by the user");
        }

        return gameRepository.save(game);
    }

    @Override
    public Game save(Game game) {
        if (!gameRepository.hasActiveGame(game.getUser())) {
            throw new NotFoundException("User has no active games");
        }

        if (game.getUser().getId() != authenticationService.getUser().getId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return gameRepository.save(game);
    }
}
