package com.lingotrainer.api.infrastructure.mapper;

import com.lingotrainer.api.infrastructure.config.MapperConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigurationInitalizer implements CommandLineRunner {

    private final ModelMapper modelMapper;

    public MapperConfigurationInitalizer(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) {
        MapperConfiguration[] mapperConfiguration = {
                new UserMapperConfiguration(),
                new GameMapperConfiguration()
        };

        for (MapperConfiguration configuration : mapperConfiguration) {
            configuration.execute(modelMapper);
        }
    }
}
