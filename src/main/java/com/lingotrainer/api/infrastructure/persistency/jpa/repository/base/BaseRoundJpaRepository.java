package com.lingotrainer.api.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.repository.RoundRepository;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.repository.RoundJpaRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class BaseRoundJpaRepository implements RoundRepository {
    private RoundJpaRepository roundJpaRepository;
    private final ModelMapper modelMapper;

    public BaseRoundJpaRepository(RoundJpaRepository roundJpaRepository, ModelMapper modelMapper) {
        this.roundJpaRepository = roundJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Round> findCurrentRound(int gameId) {
        return Optional.ofNullable(this.modelMapper.map(this.roundJpaRepository.findCurrentRound(gameId), Round.class));
    }

    @Override
    public Round save(Round round) {
        return this.modelMapper.map(this.roundJpaRepository.save(this.modelMapper.map(round, RoundEntity.class)), Round.class);
    }
}
