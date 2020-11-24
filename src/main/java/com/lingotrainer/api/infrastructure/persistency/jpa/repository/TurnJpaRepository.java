package com.lingotrainer.api.infrastructure.persistency.jpa.repository;

import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TurnJpaRepository extends JpaRepository<TurnEntity, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.guessedWord = null AND u.round.id = :roundId")
    TurnEntity findCurrentTurn(@Param("roundId") int roundId);

    TurnEntity findById(int turnEntity);
}
