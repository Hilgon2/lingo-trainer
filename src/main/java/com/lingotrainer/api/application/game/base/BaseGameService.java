package com.lingotrainer.api.application.game.base;

import com.lingotrainer.api.application.game.GameService;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.util.exception.DuplicateException;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseGameService implements GameService {

    private final AuthenticationService authenticationService;
    private GameRepository gameRepository;

    public BaseGameService(AuthenticationService authenticationService, GameRepository gameRepository) {
        this.authenticationService = authenticationService;
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Game> findById(int id) {
        return gameRepository.findById(id);
    }

    @Override
    public int createNewGame(Game game) {
        if (this.gameRepository.hasActiveGame(authenticationService.getUser().getUserId())) {
            throw new DuplicateException("An active game by the user already exists");
        }

        return this.save(game);
    }

    @Override
    public Optional<Game> findActiveGame(int userId) {
        return this.gameRepository.findActiveGame(userId);
    }

    @Override
    public int save(Game game) {
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return this.gameRepository.save(game);
    }
}
