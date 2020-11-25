package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.util.mapper.RoundMapper;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.repository.RoundRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.RoundJpaRepository;

import java.util.Optional;

public class BaseRoundJpaRepository implements RoundRepository {
    private RoundJpaRepository roundJpaRepository;
    private RoundMapper roundMapper;

    public BaseRoundJpaRepository(RoundJpaRepository roundJpaRepository, RoundMapper roundMapper) {
        this.roundJpaRepository = roundJpaRepository;
        this.roundMapper = roundMapper;
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        if (this.roundJpaRepository.findCurrentRound(gameId) == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.roundMapper.convertToDomainEntity(this.roundJpaRepository.findCurrentRound(gameId)));
    }

    @Override
    public Round save(Round round) {
        return this.roundMapper.convertToDomainEntity(this.roundJpaRepository.save(this.roundMapper.convertToPersistableEntity(round)));
    }

    @Override
    public Optional<Round> findById(int roundId) {
        return Optional.ofNullable(this.roundMapper.convertToDomainEntity(this.roundJpaRepository.findById(roundId)));
    }
}
