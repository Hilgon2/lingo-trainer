package com.lingotrainer.api.turn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TurnRepository extends JpaRepository<Turn, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.guessedWord = null AND u.round.id = :roundId order by u.id ASC")
    Optional<Turn> findCurrentTurn(@Param("roundId") int roundId);
}
