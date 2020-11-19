package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.domain.repository.TurnRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class BaseTurnJpaRepository implements TurnRepository {
    TurnJpaRepository turnJpaRepository;
    ModelMapper modelMapper;

    public BaseTurnJpaRepository(TurnJpaRepository turnJpaRepository, ModelMapper modelMapper) {
        this.turnJpaRepository = turnJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Turn> findCurrentTurn(int roundId) {
        return Optional.ofNullable(this.modelMapper.map(this.turnJpaRepository.findCurrentTurn(roundId), Turn.class));
    }

    @Override
    public Turn save(Turn turn) {
        TurnEntity turnEntity = this.turnJpaRepository.save(this.modelMapper.map(turn, TurnEntity.class));
        return this.modelMapper.map(turnEntity, Turn.class);
    }
}
