package com.lingotrainer.application.user;

import com.lingotrainer.domain.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    /**
     * Save a user, this could be either a new user creation or update of an existing user.
     * @param user the user to be saved
     * @return user information of the saved user
     */
    User save(User user);

    /**
     * Create a new user. This method is different than the save method.
     * This method contains multiple checks for a new user.
     * @param user the user to be saved
     * @return user information of the saved user
     */
    User createNewUser(User user, User currentUser);

    /**
     *  Get user information by username.
     * @param username username to look for
     * @return returns user information
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Get user by ID.
     * @param userId user ID to look for
     * @return returns user information
     */
    User findById(int userId);

    /**
     * Get user by username.
     * @param username username to look for
     * @return returns user information object
     */
    User findByUsername(String username);

    /**
     * Get a list of top users by it's highscores
     * @return list of users by highscore
     */
    List<User> retrieveTopHighscores();
}
