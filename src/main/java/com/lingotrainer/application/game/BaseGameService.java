package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BaseGameService implements GameService {

    private GameRepository gameRepository;
    private AuthenticationService authenticationService;
    private final ClassLoader classLoader = getClass().getClassLoader();

    public BaseGameService(GameRepository gameRepository,
                           AuthenticationService authenticationService) {
        this.gameRepository = gameRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Game findById(int id) {
        return gameRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Game ID %d could not be found", id)));
    }

    @Override
    public Game createNewGame(String language) {
        if (this.gameRepository.hasActiveGame(authenticationService.getUser().getUserId())) {
            throw new DuplicateException("Er is nog een lopende game");
        }

        if (!new File(this.classLoader.getResource(".").getFile() + String.format("dictionary/%s.json", language)).exists()) {
            throw new NotFoundException(String.format("Woordenlijst '%s' kon niet worden gevonden.", language));
        }

        User user = this.authenticationService.getUser();

        Game game = Game.builder()
                .userId(new UserId(user.getUserId()))
                .language(language)
                .gameStatus(GameStatus.ACTIVE)
                .build();

        return this.save(game);
    }

    @Override
    public Game findActiveGame(int userId) {
        return this.gameRepository.findActiveGame(userId).orElse(null);
    }

    @Override
    public Game save(Game game) {
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("Deze game is niet gekoppeld aan de huidige gebruiker");
        }

        return this.gameRepository.save(game);
    }

    @Override
    public boolean hasActiveGame(int userId) {
        return this.gameRepository.hasActiveGame(userId);
    }
}
