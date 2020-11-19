package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameJpaRepository extends JpaRepository<GameEntity, Integer> {
    @Query(value = "SELECT case when count(u) > 0 then true else false end FROM #{#entityName} u WHERE u.gameStatus = 'ACTIVE' AND u.user = :user")
    boolean hasActiveGame(@Param("user") User user);
}
