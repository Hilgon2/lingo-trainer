package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoundMapper implements EntityMapper<Round, RoundEntity> {

    public Round convertToDomainEntity(RoundEntity roundEntity) {
        Round newRound = Round.builder()
                .roundId(new RoundId(roundEntity.getId()))
                .gameId(new GameId(roundEntity.getGame().getId()))
                .word(roundEntity.getWord())
                .active(roundEntity.isActive())
                .build();

        if (roundEntity.getTurns() != null) {
            newRound.setTurnIds(roundEntity.getTurns().stream().map(turn ->
                    new TurnId(turn.getId())).collect(Collectors.toList()));
        }

        return newRound;
    }

    public RoundEntity convertToPersistableEntity(Round round) {
        RoundEntity newRound = RoundEntity.builder()
                .id(round.getRoundId())
                .word(round.getWord())
                .active(round.isActive())
                .build();

        if (round.getTurnIds() != null) {
            newRound.setTurns(round.getTurnIds()
                    .stream()
                    .map(turnId -> TurnEntity.builder()
                            .id(turnId.getId())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (round.getGameId() != 0) {
            newRound.setGame(GameEntity.builder().id(round.getGameId()).build());
        }

        return newRound;
    }

    public List<RoundEntity> convertToPersistableEntities(List<Round> rounds) {
        List<RoundEntity> roundEntities = new ArrayList<>();

        for (Round round : rounds) {
            RoundEntity newRound = RoundEntity.builder()
                    .id(round.getRoundId())
                    .game(GameEntity.builder().id(round.getGameId()).build())
                    .word(round.getWord())
                    .active(round.isActive())
                    .build();

            if (round.getTurnIds() != null) {
                newRound.setTurns(round.getTurnIds()
                        .stream()
                        .map(turnId -> TurnEntity.builder()
                                .id(turnId.getId())
                                .build())
                        .collect(Collectors.toList()));
            }

            roundEntities.add(newRound);
        }


        return roundEntities;
    }

    public List<Round> convertToDomainEntities(List<RoundEntity> roundEntities) {
        List<Round> rounds = new ArrayList<>();

        for (RoundEntity roundEntity : roundEntities) {
            Round newRound = Round.builder()
                    .roundId(new RoundId(roundEntity.getId()))
                    .gameId(new GameId(roundEntity.getGame().getId()))
                    .word(roundEntity.getWord())
                    .active(roundEntity.isActive())
                    .build();

            if (roundEntity.getTurns() != null) {
                newRound.setTurnIds(roundEntity.getTurns().stream().map(turn ->
                        new TurnId(turn.getId())).collect(Collectors.toList()));
            }

            rounds.add(newRound);
        }

        return rounds;
    }
}
