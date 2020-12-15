package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TurnMapperTest {

    private TurnMapper turnMapperUnderTest;

    private static Turn turn;
    private static TurnEntity turnEntity;
    private static List<Turn> turns;
    private static List<TurnEntity> turnEntities;

    @BeforeAll
    static void init() {
        turn = Turn
                .builder()
                .turnId(new TurnId(1))
                .roundId(new RoundId(1))
                .guessedWord("schoen")
                .build();

        turnEntity = TurnEntity
                .builder()
                .id(1)
                .round(RoundEntity
                        .builder()
                        .id(1)
                        .turns(new ArrayList<>())
                        .build())
                .guessedWord("schoen")
                .build();

        turns = List.of(
                Turn
                        .builder()
                        .turnId(new TurnId(4))
                        .roundId(new RoundId(1))
                        .guessedWord("schoen")
                        .build(),
                Turn
                        .builder()
                        .turnId(new TurnId(5))
                        .roundId(new RoundId(1))
                        .guessedWord("schoen")
                        .build(),
                Turn
                        .builder()
                        .turnId(new TurnId(6))
                        .roundId(new RoundId(1))
                        .guessedWord("schoen")
                        .build()
        );

        turnEntities = List.of(
                TurnEntity
                        .builder()
                        .id(4)
                        .round(RoundEntity
                                .builder()
                                .id(1)
                                .turns(new ArrayList<>())
                                .build())
                        .guessedWord("schoen")
                        .build(),
                TurnEntity
                        .builder()
                        .id(5)
                        .round(RoundEntity
                                .builder()
                                .id(1)
                                .turns(new ArrayList<>())
                                .build())
                        .guessedWord("schoen")
                        .build(),
                TurnEntity
                        .builder()
                        .id(6)
                        .round(RoundEntity
                                .builder()
                                .id(1)
                                .turns(new ArrayList<>())
                                .build())
                        .guessedWord("schoen")
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        turnMapperUnderTest = new TurnMapper();
    }

    static Stream<Arguments> provideConvertToDomainEntity() {
        return Stream.of(
                Arguments.of(turnEntity, turn)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntity")
    @DisplayName("Convert to domain entity")
    void test_convert_to_domain_entity(TurnEntity turnEntity, Turn expectedResult) {
        // Run the test
        final Turn result = turnMapperUnderTest.convertToDomainEntity(turnEntity);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntity() {
        return Stream.of(
                Arguments.of(turn, turnEntity)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntity")
    @DisplayName("Convert to persistable entity")
    void test_convert_to_persistable_entity(Turn turn, TurnEntity expectedResult) {
        // Run the test
        final TurnEntity result = turnMapperUnderTest.convertToPersistableEntity(turn);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToPersistableEntities() {
        return Stream.of(
                Arguments.of(turns, turnEntities)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToPersistableEntities")
    @DisplayName("Convert to persistable entities")
    void test_convert_to_persistable_entities(List<Turn> turns, List<TurnEntity> expectedResult) {
        // Run the test
        final List<TurnEntity> result = turnMapperUnderTest.convertToPersistableEntities(turns);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToDomainEntities() {
        return Stream.of(
                Arguments.of(turnEntities, turns)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToDomainEntities")
    @DisplayName("Convert to domain entities")
    void test_convert_to_domain_entities(List<TurnEntity> turnEntities, List<Turn> expectedResult) {
        // Run the test
        final List<Turn> result = turnMapperUnderTest.convertToDomainEntities(turnEntities);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
