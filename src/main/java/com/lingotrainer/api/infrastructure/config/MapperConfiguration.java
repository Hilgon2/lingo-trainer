package com.lingotrainer.api.infrastructure.config;

import org.modelmapper.ModelMapper;

public interface MapperConfiguration {
    void execute(ModelMapper modelMapper);
}