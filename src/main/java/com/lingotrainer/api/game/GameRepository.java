package com.lingotrainer.api.game;

import com.lingotrainer.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query(value = "SELECT case when count(u) > 0 then true else false end FROM #{#entityName} u WHERE u.gameStatus = 'ACTIVE' AND u.user = :user")
    boolean hasActiveGame(@Param("user") User user);
}
