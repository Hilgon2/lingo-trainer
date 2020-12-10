package com.lingotrainer.application.user;

import com.lingotrainer.domain.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    User save(User user);
    UserDetails loadUserByUsername(String username);
    User findById(int id);
    User findByUsername(String username);
    User createNewUser(User user, User currentUser);
}
