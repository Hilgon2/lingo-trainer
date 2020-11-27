package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;

import java.util.ArrayList;
import java.util.List;

public class TurnMapper implements EntityMapper<Turn, TurnEntity> {

    public Turn convertToDomainEntity(TurnEntity turnEntity) {
        return Turn.builder()
                .turnId(new TurnId(turnEntity.getId()))
                .startedAt(turnEntity.getStartedAt())
                .roundId(new RoundId(turnEntity.getRound().getId()))
                .guessedWord(turnEntity.getGuessedWord())
                .build();
    }

    public TurnEntity convertToPersistableEntity(Turn turn) {
        return TurnEntity.builder()
                .id(turn.getTurnId())
                .startedAt(turn.getStartedAt())
                .round(RoundEntity.builder().id(turn.getRoundId()).build())
                .guessedWord(turn.getGuessedWord())
                .build();
    }

    public List<TurnEntity> convertToPersistableEntities(List<Turn> turns) {
        List<TurnEntity> turnEntities = new ArrayList<>();

        turns.forEach(turn -> TurnEntity.builder()
                .id(turn.getTurnId())
                .guessedWord(turn.getGuessedWord())
                .startedAt(turn.getStartedAt())
                .round(RoundEntity.builder().id(turn.getRoundId()).build())
                .build());

        return turnEntities;
    }

    public List<Turn> convertToDomainEntities(List<TurnEntity> turnEntities) {
        List<Turn> turns = new ArrayList<>();

        turnEntities.forEach(turnEntity -> turns.add(Turn.builder()
                .turnId(new TurnId(turnEntity.getId()))
                .guessedWord(turnEntity.getGuessedWord())
                .startedAt(turnEntity.getStartedAt())
                .roundId(new RoundId(turnEntity.getRound().getId()))
                .build()));

        return turns;
    }
}
