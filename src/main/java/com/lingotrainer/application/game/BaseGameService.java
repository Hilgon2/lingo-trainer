package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.application.authentication.AuthenticationService;
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

    /**
     * Get the game information.
     * @param id the id of the game to be found
     * @return game object, containing game information
     */
    @Override
    public Optional<Game> findById(int id) {
        return gameRepository.findById(id);
    }

    /**
     * Create new game for the logged in user, based on the language code.
     * @param languageCode language of the dictionary
     * @return game ID of the newly created game
     */
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

    /**
     * Get the active game of a user.
     * @param userId the ID of the user
     * @return game object, containing game information
     */
    @Override
    public Optional<Game> findActiveGame(int userId) {
        return this.gameRepository.findActiveGame(userId);
    }


    /**
     * Create or update a game, depending on if the game already exists or not.
     * @param game game object to be saved
     * @return ID of the game which was saved
     */
    @Override
    public int save(Game game) {
        if (game.getUserId() != authenticationService.getUser().getUserId()) {
            throw new ForbiddenException("This game is not linked to the current user");
        }

        return this.gameRepository.save(game);
    }
}
