package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import com.lingotrainer.api.domain.repository.TurnRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import com.lingotrainer.api.util.mapper.TurnMapper;

import java.util.Optional;

public class BaseTurnJpaRepository implements TurnRepository {
    TurnJpaRepository turnJpaRepository;
    TurnMapper turnMapper;

    public BaseTurnJpaRepository(TurnJpaRepository turnJpaRepository, TurnMapper turnMapper) {
        this.turnJpaRepository = turnJpaRepository;
        this.turnMapper = turnMapper;
    }

    @Override
    public Optional<Turn> findCurrentTurn(int roundId) {
        return Optional.ofNullable(this.turnMapper.convertToDomainEntity((this.turnJpaRepository.findCurrentTurn(roundId))));
    }

    @Override
    public Turn save(Turn turn) {
        TurnEntity turnEntity = this.turnJpaRepository.save(this.turnMapper.convertToPersistableEntity(turn));
        return this.turnMapper.convertToDomainEntity(turnEntity);
    }

    @Override
    public Optional<Turn> findById(int turnId) {
        return Optional.ofNullable(this.turnMapper.convertToDomainEntity(this.turnJpaRepository.findById(turnId)));
    }
}
