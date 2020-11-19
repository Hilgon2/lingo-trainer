package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoundJpaRepository extends JpaRepository<RoundEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.game.id = :gameId AND u.id = (SELECT MAX(v.id) FROM #{#entityName} v WHERE v.game.id = :gameId)")
    Optional<RoundEntity> findCurrentRound(@Param("gameId") int gameId);
}
