package com.lingotrainer.api.repository;

import com.lingotrainer.api.model.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnRepository extends JpaRepository<Turn, Integer> {
}
