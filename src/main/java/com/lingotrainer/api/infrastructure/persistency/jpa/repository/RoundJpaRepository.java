package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoundJpaRepository extends JpaRepository<RoundEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.game.id = :gameId AND u.active = 1")
    RoundEntity findCurrentRound(@Param("gameId") int gameId);

    RoundEntity findById(int roundId);
}
