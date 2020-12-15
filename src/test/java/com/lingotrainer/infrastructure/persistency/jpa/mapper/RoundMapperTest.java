package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import com.lingotrainer.domain.model.user.Role;
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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundMapperTest {

    private RoundMapper roundMapperUnderTest;

    private static Round round;
    private static RoundEntity roundEntity;
    private static List<Round> rounds;
    private static List<RoundEntity> roundEntities;

    @BeforeAll
    static void setup() {
        round = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .turnIds(List.of(new TurnId(1)))
                .word("schaap")
                .build();

        roundEntity = RoundEntity
                .builder()
                .id(1)
                .game(GameEntity
                        .builder()
                        .id(1)
                        .build())
                .turns(List.of(TurnEntity
                        .builder()
                        .id(1)
                        .build()))
                .word("schaap")
                .build();

        rounds = List.of(
                Round
                        .builder()
                        .roundId(new RoundId(2))
                        .gameId(new GameId(2))
                        .turnIds(List.of(new TurnId(6)))
                        .word("varken")
                        .build(),
                Round
                        .builder()
                        .roundId(new RoundId(3))
                        .gameId(new GameId(2))
                        .turnIds(List.of(new TurnId(7)))
                        .word("koeien")
                        .build(),
                Round
                        .builder()
                        .roundId(new RoundId(4))
                        .gameId(new GameId(2))
                        .turnIds(List.of(new TurnId(8)))
                        .word("schaap")
                        .build()
        );

        roundEntities = List.of(
                RoundEntity
                        .builder()
                        .id(2)
                        .game(GameEntity
                                .builder()
                                .id(2)
                                .build())
                        .turns(List.of(
                                TurnEntity
                                .builder()
                                .id(6)
                                .build()
                        ))
                        .word("varken")
                        .build(),
                RoundEntity
                        .builder()
                        .id(3)
                        .game(GameEntity
                                .builder()
                                .id(2)
                                .build())
                        .turns(List.of(
                                TurnEntity
                                        .builder()
                                        .id(7)
                                        .build()
                        ))
                        .word("koeien")
                        .build(),
                RoundEntity
                        .builder()
                        .id(4)
                        .game(GameEntity
                                .builder()
                                .id(2)
                                .build())
                        .turns(List.of(
                                TurnEntity
                                        .builder()
                                        .id(8)
                                        .build()
                        ))
                        .word("schaap")
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        roundMapperUnderTest = new RoundMapper();
    }

    static Stream<Arguments> provideConvertToDomainEntity() {
        return Stream.of(
                Arguments.of(roundEntity, round)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntity")
    @DisplayName("Convert to domain entity")
    void testConvertToDomainEntity(RoundEntity roundEntity, Round expectedResult) {
        // Run the test
        final Round result = roundMapperUnderTest.convertToDomainEntity(roundEntity);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntity() {
        return Stream.of(
                Arguments.of(round, roundEntity)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntity")
    @DisplayName("Convert to persistable entity")
    void testConvertToPersistableEntity(Round round, RoundEntity expectedResult) {
        // Run the test
        final RoundEntity result = roundMapperUnderTest.convertToPersistableEntity(round);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntities() {
        return Stream.of(
                Arguments.of(rounds, roundEntities)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntities")
    @DisplayName("Convert to persistable entities")
    void testConvertToPersistableEntities(List<Round> rounds, List<RoundEntity> expectedResult) {
        // Run the test
        final List<RoundEntity> result = roundMapperUnderTest.convertToPersistableEntities(rounds);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToDomainEntities() {
        return Stream.of(
                Arguments.of(roundEntities, rounds)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntities")
    @DisplayName("Convert to domain entities")
    void testConvertToDomainEntities(List<RoundEntity> roundEntities, List<Round> expectedResult) {
        // Run the test
        final List<Round> result = roundMapperUnderTest.convertToDomainEntities(roundEntities);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
