package com.lingotrainer.api.util.mapper;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.GameId;
import com.lingotrainer.api.domain.model.game.round.RoundId;
import com.lingotrainer.api.domain.model.user.UserId;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameMapper implements EntityMapper<Game, GameEntity> {

    @Override
    public Game convertToDomainEntity(GameEntity gameEntity) {
        Game game = Game.builder()
                .gameId(new GameId(gameEntity.getId()))
                .gameStatus(gameEntity.getGameStatus())
                .language(gameEntity.getLanguage())
                .userId(new UserId(gameEntity.getUser().getId()))
                .score(gameEntity.getScore())
                .build();

        if (gameEntity.getRounds() != null) {
            game.setRoundIds(gameEntity.getRounds().stream().map(round -> new RoundId(round.getId())).collect(Collectors.toList()));
        }

        return game;
    }

    @Override
    public GameEntity convertToPersistableEntity(Game game) {
        GameEntity gameEntity = GameEntity.builder()
                .id(game.getGameId())
                .gameStatus(game.getGameStatus())
                .language(game.getLanguage())
                .user(UserEntity.builder().id(game.getUserId()).build())
                .score(game.getScore())
                .build();

        if (game.getRoundIds() != null) {
            gameEntity.setRounds(game.getRoundIds().stream().map(roundId -> RoundEntity.builder().id(roundId.getId()).build()).collect(Collectors.toList()));
        }

        return gameEntity;
    }

    @Override
    public List<GameEntity> convertToPersistableEntities(List<Game> games) {
        List<GameEntity> gameEntities = new ArrayList<>();

        for (Game game : games) {
            GameEntity newGame = GameEntity.builder()
                    .id(game.getGameId())
                    .gameStatus(game.getGameStatus())
                    .language(game.getLanguage())
                    .user(UserEntity.builder().id(game.getUserId()).build())
                    .score(game.getScore())
                    .build();
            if (game.getRoundIds() != null) {
                newGame.setRounds(game.getRoundIds().stream().map(roundId -> RoundEntity.builder().id(roundId.getId()).build()).collect(Collectors.toList()));
            }
            gameEntities.add(newGame);
        }

        return gameEntities;
    }

    @Override
    public List<Game> convertToDomainEntities(List<GameEntity> gameEntities) {
        List<Game> games = new ArrayList<>();

        for (GameEntity gameEntity : gameEntities) {
            Game newGame = Game.builder()
                    .gameId(new GameId(gameEntity.getId()))
                    .gameStatus(gameEntity.getGameStatus())
                    .language(gameEntity.getLanguage())
                    .userId(new UserId(gameEntity.getUser().getId()))
                    .score(gameEntity.getScore())
                    .build();

            if (gameEntity.getRounds() != null) {
                newGame.setRoundIds(gameEntity.getRounds().stream().map(roundId -> new RoundId(roundId.getId())).collect(Collectors.toList()));
            }

            games.add(newGame);
        }

        return games;
    }
}
