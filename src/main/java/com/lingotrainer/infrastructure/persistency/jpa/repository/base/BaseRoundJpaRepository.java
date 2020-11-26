package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.RoundMapper;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.infrastructure.persistency.jpa.repository.RoundJpaRepository;

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
        RoundEntity roundEntity = this.roundJpaRepository.findCurrentRound(gameId);
        if (roundEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.roundMapper.convertToDomainEntity(roundEntity));
    }

    @Override
    public Optional<Round> findLastRound(int gameId) {
        RoundEntity roundEntity = this.roundJpaRepository.findLastRound(gameId);
        if (roundEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.roundMapper.convertToDomainEntity(roundEntity));
    }

    @Override
    public Round save(Round round) {
        return this.roundMapper.convertToDomainEntity(
                this.roundJpaRepository.save(this.roundMapper.convertToPersistableEntity(round))
        );
    }

    @Override
    public Optional<Round> findById(int roundId) {
        RoundEntity roundEntity = this.roundJpaRepository.findById(roundId);

        if (roundEntity == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.roundMapper.convertToDomainEntity(roundEntity));
    }
}
