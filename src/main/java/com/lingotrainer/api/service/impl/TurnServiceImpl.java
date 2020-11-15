package com.lingotrainer.api.service.impl;

import com.lingotrainer.api.model.Turn;
import com.lingotrainer.api.repository.TurnRepository;
import com.lingotrainer.api.service.TurnService;
import org.springframework.stereotype.Service;


@Service
public class TurnServiceImpl implements TurnService {

    private TurnRepository turnRepository;

    public TurnServiceImpl(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    @Override
    public Turn save(Turn turn) {
        return turnRepository.save(turn);
    }
}
