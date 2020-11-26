package com.lingotrainer.api.application.game;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.GameStatus;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.model.user.UserId;
import com.lingotrainer.api.domain.repository.GameRepository;
import com.lingotrainer.api.util.exception.DuplicateException;
import com.lingotrainer.api.util.exception.ForbiddenException;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public int createNewGame(String languageCode) {
        if (this.gameRepository.hasActiveGame(authenticationService.getUser().getUserId())) {
            throw new DuplicateException("An active game by the user already exists");
        }

        if (!new File(String.format("src/main/resources/dictionary/%s.json", languageCode)).exists()) {
            throw new NotFoundException(String.format("Language code '%s' not found.", languageCode));
        }

        User user = this.authenticationService.getUser();

        Game game = Game.builder()
                .userId(new UserId(user.getUserId()))
                .language(languageCode)
                .gameStatus(GameStatus.ACTIVE)
                .build();

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
