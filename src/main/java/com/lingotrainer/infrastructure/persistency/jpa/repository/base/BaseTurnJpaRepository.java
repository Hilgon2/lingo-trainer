package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.repository.TurnRepository;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.EntityMapper;
import com.lingotrainer.infrastructure.persistency.jpa.repository.TurnJpaRepository;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.TurnMapper;

import java.util.List;
import java.util.Optional;

public class BaseTurnJpaRepository implements TurnRepository {
    private TurnJpaRepository turnJpaRepository;
    private EntityMapper<Turn, TurnEntity> turnMapper;

    public BaseTurnJpaRepository(TurnJpaRepository turnJpaRepository, TurnMapper turnMapper) {
        this.turnJpaRepository = turnJpaRepository;
        this.turnMapper = turnMapper;
    }

    @Override
    public Optional<Turn> findCurrentTurn(int roundId) {
        TurnEntity turnEntity = this.turnJpaRepository.findCurrentTurn(roundId);

        if (turnEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.turnMapper.convertToDomainEntity((turnEntity)));
    }

    @Override
    public Turn save(Turn turn) {
        TurnEntity turnEntity = this.turnJpaRepository.save(this.turnMapper.convertToPersistableEntity(turn));

        return this.turnMapper.convertToDomainEntity(turnEntity);
    }

    @Override
    public Optional<Turn> findById(int turnId) {
        TurnEntity turnEntity = this.turnJpaRepository.findById(turnId);

        if (turnEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.turnMapper.convertToDomainEntity(this.turnJpaRepository.findById(turnId)));
    }

    @Override
    public List<Turn> findActiveTurnsByRoundId(int roundId) {
        return this.turnMapper.convertToDomainEntities(this.turnJpaRepository.findActiveTurnsByRoundId(roundId));
    }
}
