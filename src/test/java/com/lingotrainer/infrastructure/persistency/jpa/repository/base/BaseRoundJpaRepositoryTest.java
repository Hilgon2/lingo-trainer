package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.RoundMapper;
import com.lingotrainer.infrastructure.persistency.jpa.repository.RoundJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BaseRoundJpaRepositoryTest {

    @Mock
    private RoundJpaRepository mockRoundJpaRepository;
    @Mock
    private RoundMapper mockRoundMapper;

    private BaseRoundJpaRepository baseRoundJpaRepositoryUnderTest;

    private static RoundEntity roundEntity;
    private static Round round;
    private static GameEntity game;

    private final static String language = "test-nl_nl";

    @BeforeAll
    static void init() {
        UserEntity user = UserEntity
                .builder()
                .id(1)
                .username("terry")
                .password("wachtwoord")
                .build();

        game = GameEntity
                .builder()
                .id(1)
                .user(user)
                .language(language)
                .gameStatus(GameStatus.ACTIVE)
                .build();

        roundEntity = RoundEntity
                .builder()
                .id(1)
                .word("schaap")
                .game(game)
                .active(true)
                .build();

        round = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .word("schaap")
                .build();
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        baseRoundJpaRepositoryUnderTest = new BaseRoundJpaRepository(mockRoundJpaRepository, mockRoundMapper);
    }

    static Stream<Arguments> provideCurrentRound() {
        return Stream.of(
                Arguments.of( // current round found
                        1,
                        roundEntity,
                        round,
                        Optional.ofNullable(Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .word("schaap")
                                .build())
                ),
                Arguments.of(1, null, round, Optional.empty()) // no current round found
        );
    }

    @ParameterizedTest
    @MethodSource("provideCurrentRound")
    @DisplayName("Find current round")
    void test_find_current_round(int gameId, RoundEntity roundEntity, Round round, Optional<Round> expectedResult) {
        when(mockRoundJpaRepository.findCurrentRound(gameId)).thenReturn(roundEntity);
        when(mockRoundMapper.convertToDomainEntity(roundEntity)).thenReturn(round);

        // Run the test
        final Optional<Round> result = baseRoundJpaRepositoryUnderTest.findCurrentRound(gameId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideLastRound() {
        return Stream.of(
                Arguments.of( // last round found
                        1,
                        RoundEntity
                                .builder()
                                .id(2)
                                .word("lopen")
                                .game(game)
                                .active(true)
                                .build(),
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .word("lopen")
                                .build(),
                        Optional.ofNullable(Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .word("lopen")
                                .build())
                ),
                Arguments.of( // last round not found
                        1,
                        null,
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .word("lopen")
                                .build(),
                        Optional.empty()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideLastRound")
    @DisplayName("Find last round")
    void test_find_last_round(int gameId, RoundEntity roundEntity, Round round, Optional<Round> expectedResult) {
        when(mockRoundJpaRepository.findLastRound(gameId)).thenReturn(roundEntity);
        when(mockRoundMapper.convertToDomainEntity(roundEntity)).thenReturn(round);

        // Run the test
        final Optional<Round> result = baseRoundJpaRepositoryUnderTest.findLastRound(gameId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideSaveRound() {
        return Stream.of(
                Arguments.of(
                        roundEntity,
                        round,
                        Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .word("schaap")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveRound")
    @DisplayName("Save a round")
    void testSave(RoundEntity roundEntity, Round round, Round expectedResult) {
        when(mockRoundMapper.convertToDomainEntity(roundEntity)).thenReturn(round);
        when(mockRoundJpaRepository.save(roundEntity)).thenReturn(roundEntity);
        when(mockRoundMapper.convertToPersistableEntity(round)).thenReturn(roundEntity);

        // Run the test
        final Round result = baseRoundJpaRepositoryUnderTest.save(round);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindRoundById() {
        return Stream.of(
                Arguments.of(
                        1,
                        roundEntity,
                        round,
                        Optional.ofNullable(Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .word("schaap")
                                .build())
                ),
                Arguments.of(1, null, round, Optional.empty()) // no current round found
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindRoundById")
    @DisplayName("Find a round by ID")
    void test_find_by_id(int roundId, RoundEntity roundEntity, Round round, Optional<Round> expectedResult) {
        when(mockRoundJpaRepository.findById(roundId)).thenReturn(roundEntity);
        when(mockRoundMapper.convertToDomainEntity(roundEntity)).thenReturn(round);

        // Run the test
        final Optional<Round> result = baseRoundJpaRepositoryUnderTest.findById(roundId);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
