package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameMapperTest {

    private GameMapper gameMapperUnderTest;

    private static Game game;
    private static GameEntity gameEntity;
    private static List<Game> games;
    private static List<GameEntity> gameEntities;

    @BeforeAll
    static void setup() {
        UserEntity userEntity =
                UserEntity
                        .builder()
                        .id(1)
                        .build();

        gameEntity = GameEntity
                .builder()
                .id(1)
                .user(userEntity)
                .language("nl_nl")
                .gameStatus(GameStatus.ACTIVE)
                .rounds(List.of(
                        RoundEntity
                                .builder()
                                .id(1)
                                .build()
                ))
                .build();

        game = Game
                .builder()
                .gameId(new GameId(1))
                .userId(new UserId(1))
                .language("nl_nl")
                .gameStatus(GameStatus.ACTIVE)
                .roundIds(List.of(new RoundId(1)))
                .build();

        games = List.of(
                Game
                        .builder()
                        .gameId(new GameId(2))
                        .userId(new UserId(1))
                        .language("nl_nl")
                        .gameStatus(GameStatus.FINISHED)
                        .roundIds(List.of(new RoundId(1)))
                        .build(),
                Game
                        .builder()
                        .gameId(new GameId(3))
                        .userId(new UserId(1))
                        .language("nl_nl")
                        .gameStatus(GameStatus.FINISHED)
                        .roundIds(List.of(new RoundId(2)))
                        .build(),
                Game
                        .builder()
                        .gameId(new GameId(4))
                        .userId(new UserId(1))
                        .language("nl_nl")
                        .gameStatus(GameStatus.ACTIVE)
                        .roundIds(List.of(new RoundId(3)))
                        .build()
        );

        gameEntities = List.of(
                GameEntity
                        .builder()
                        .id(2)
                        .user(userEntity)
                        .language("nl_nl")
                        .gameStatus(GameStatus.FINISHED)
                        .rounds(List.of(
                                RoundEntity
                                        .builder()
                                        .id(1)
                                        .build()
                        ))
                        .build(),
                GameEntity
                        .builder()
                        .id(3)
                        .user(userEntity)
                        .language("nl_nl")
                        .gameStatus(GameStatus.FINISHED)
                        .rounds(List.of(
                                RoundEntity
                                        .builder()
                                        .id(2)
                                        .build()
                        ))
                        .build(),
                GameEntity
                        .builder()
                        .id(4)
                        .user(userEntity)
                        .language("nl_nl")
                        .gameStatus(GameStatus.ACTIVE)
                        .rounds(List.of(
                                RoundEntity
                                        .builder()
                                        .id(3)
                                        .build()
                        ))
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        gameMapperUnderTest = new GameMapper();
    }

    static Stream<Arguments> provideConvertToDomainEntity() {
        return Stream.of(
                Arguments.of(gameEntity, game)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntity")
    @DisplayName("Convert to domain entity")
    void test_convert_to_domain_entity(GameEntity gameEntity, Game expectedResult) {
        // Run the test
        final Game result = gameMapperUnderTest.convertToDomainEntity(gameEntity);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntity() {
        return Stream.of(
                Arguments.of(game, gameEntity)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntity")
    @DisplayName("Convert to persistable entity")
    void testConvertToPersistableEntity(Game game, GameEntity expectedResult) {
        // Run the test
        final GameEntity result = gameMapperUnderTest.convertToPersistableEntity(game);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntities() {
        return Stream.of(
                Arguments.of(games, gameEntities)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntities")
    @DisplayName("Convert to persistable entities")
    void testConvertToPersistableEntities(List<Game> games, List<GameEntity> expectedResult) {
        // Run the test
        final List<GameEntity> result = gameMapperUnderTest.convertToPersistableEntities(games);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToDomainEntities() {
        return Stream.of(
                Arguments.of(gameEntities, games)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntities")
    @DisplayName("Convert to domain entities")
    void testConvertToDomainEntities(List<GameEntity> gameEntities, List<Game> expectedResult) {
        // Run the test
        final List<Game> result = gameMapperUnderTest.convertToDomainEntities(gameEntities);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
