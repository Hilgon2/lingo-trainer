package com.lingotrainer.application.game;

import com.lingotrainer.domain.model.game.Game;

public interface GameService {
    /**
     * Get the game information.
     * @param id the id of the game to be found
     * @return game object, containing game information
     */
    Game findById(int id);

    /**
     * Create new game for the logged in user, based on the language code.
     * @param languageCode language of the dictionary
     * @return game ID of the newly created game
     */
    Game createNewGame(String languageCode);

    /**
     * Get the active game of a user.
     * @param userId the ID of the user
     * @return game object, containing game information
     */
    Game findActiveGame(int userId);

    /**
     * Check if user has an active game.
     * @param userId the ID of the user to be checked
     * @return returns true or false
     */
    boolean hasActiveGame(int userId);

    /**
     * Create or update a game, depending on if the game already exists or not.
     * @param game game object to be saved
     * @return ID of the game which was saved
     */
    Game save(Game game);
}
