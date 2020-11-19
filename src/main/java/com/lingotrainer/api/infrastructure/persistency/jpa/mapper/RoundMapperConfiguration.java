package com.lingotrainer.api.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.infrastructure.config.MapperConfiguration;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import org.modelmapper.ModelMapper;

public class RoundMapperConfiguration implements MapperConfiguration {
    @Override
    public void execute(ModelMapper modelMapper) {
        modelMapper.createTypeMap(RoundEntity.class, Round.class);
    }
}
