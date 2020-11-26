package com.lingotrainer.infrastructure.persistency.jpa.repository;

import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoundJpaRepository extends JpaRepository<RoundEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.game.id = :gameId AND u.active = 1")
    RoundEntity findCurrentRound(@Param("gameId") int gameId);

    @Query(value = "SELECT u FROM #{#entityName} u "
            + "WHERE u.game.id = :gameId AND u.id = (SELECT MAX(ul.id) FROM #{#entityName} ul WHERE ul.active = 0)")
    RoundEntity findLastRound(@Param("gameId") int gameId);

    RoundEntity findById(int roundId);
}
