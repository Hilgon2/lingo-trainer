package com.lingotrainer.api.repository;

import com.lingotrainer.api.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Integer> {
    @Query(value = "SELECT u FROM #{#entityName} u WHERE u.game.id = :gameId AND u.id = (SELECT MAX(v.id) FROM #{#entityName} v WHERE v.game.id = :gameId)")
    Optional<Round> findCurrentRound(@Param("gameId") int gameId);
}
