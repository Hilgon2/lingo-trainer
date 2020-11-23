package com.lingotrainer.api.application.game.base;

import com.lingotrainer.api.application.game.GameService;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.util.exception.DuplicateException;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseGameService implements GameService {

    private final AuthenticationService authenticationService;
    private GameRepository gameRepository;
    private final ModelMapper modelMapper;

    public BaseGameService(AuthenticationService authenticationService, GameRepository gameRepository, ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Game> findById(int id) {
        Optional<Game> game = Optional.ofNullable(this.modelMapper.map(gameRepository.findById(id), Game.class));
        return game;
    }

    @Override
    public int createNewGame(Game game) {
        if (gameRepository.hasActiveGame(authenticationService.getUser())) {
            throw new DuplicateException("An active game by the user already exists");
        }

        return this.save(game);
    }

    @Override
    public int save(Game game) {
        if (!gameRepository.hasActiveGame(game.getUser())) {
            throw new NotFoundException("User has no active games");
        }

        if (game.getUser().getId() != authenticationService.getUser().getId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return gameRepository.save(game);
    }
}
