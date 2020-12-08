package com.lingotrainer.infrastructure.persistency.jpa.repository;

import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TurnJpaRepository extends JpaRepository<TurnEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.guessedWord = null AND u.round.game.id = :gameId AND u.round.active = true")
    TurnEntity findCurrentTurn(@Param("gameId") int gameId);

    TurnEntity findById(int turnEntity);

    @Query(value = "SELECT u FROM #{#entityName} u WHERE NOT u.guessedWord = null AND u.round.id = :roundId")
    List<TurnEntity> findActiveTurnsByRoundId(@Param("roundId") int roundId);
}
