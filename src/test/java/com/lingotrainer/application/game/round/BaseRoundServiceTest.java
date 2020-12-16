package com.lingotrainer.application.game.round;

import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.dictionary.DictionaryService;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.application.game.BaseGameService;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.*;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.domain.repository.TurnRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BaseRoundServiceTest {

    @Mock
    private RoundRepository mockRoundRepository;
    @Mock
    private AuthenticationService mockAuthenticationService;
    @Mock
    private BaseGameService mockGameService;
    @Mock
    private DictionaryService mockDictionaryService;
    @Mock
    private TurnRepository mockTurnRepository;
    @Mock
    private UserService mockUserService;

    private BaseRoundService mockRoundService;

    private static User trainee;
    private static User trainee2;
    private static User admin;
    private static Round round;
    private static Round round2;
    private static Game game;
    private static Game game2;
    private static Turn turn;
    private static Turn turn2;

    private static Instant currentTime = Instant.now();

    private static final String language = "test-nl_nl";

    @BeforeEach
    void setUp() {
        initMocks(this);
        mockRoundService = new BaseRoundService(mockRoundRepository, mockAuthenticationService, mockGameService, mockDictionaryService, mockTurnRepository, mockUserService);
    }

    @BeforeAll
    static void init() {
        admin = User
                .builder()
                .userId(new UserId(1))
                .username("admin")
                .password("admin123")
                .gameIds(new ArrayList<>())
                .role(Role.ADMIN)
                .active(true)
                .highscore(0)
                .build();

        trainee = User
                .builder()
                .userId(new UserId(2))
                .username("tzhou")
                .password("terrywachtwoord")
                .gameIds(new ArrayList<>(List.of(new GameId(1))))
                .role(Role.TRAINEE)
                .active(true)
                .highscore(0)
                .build();

        trainee2 = User
                .builder()
                .userId(new UserId(3))
                .username("henk")
                .password("ditismijnwachtwoord")
                .gameIds(new ArrayList<>())
                .role(Role.TRAINEE)
                .active(true)
                .highscore(0)
                .build();

        game = Game
                .builder()
                .gameId(new GameId(1))
                .userId(new UserId(2))
                .roundIds(new ArrayList<>(List.of(new RoundId(1))))
                .language(language)
                .gameStatus(GameStatus.ACTIVE)
                .score(0)
                .build();

        game2 = Game
                .builder()
                .gameId(new GameId(2))
                .userId(new UserId(3))
                .roundIds(new ArrayList<>(List.of(new RoundId(1))))
                .language(language)
                .gameStatus(GameStatus.ACTIVE)
                .score(0)
                .build();

        round = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .turnIds(new ArrayList<>(List.of(new TurnId(1))))
                .word("dicht")
                .wordLength(WordLength.FIVE)
                .active(true)
                .build();

        round2 = Round
                .builder()
                .roundId(new RoundId(2))
                .gameId(new GameId(2))
                .turnIds(new ArrayList<>(List.of(new TurnId(2))))
                .word("schoen")
                .wordLength(WordLength.SIX)
                .active(true)
                .build();

        turn = Turn
                .builder()
                .turnId(new TurnId(1))
                .roundId(new RoundId(1))
                .startedAt(currentTime)
                .build();

        turn2 = Turn
                .builder()
                .turnId(new TurnId(2))
                .roundId(new RoundId(2))
                .startedAt(currentTime)
                .build();
    }

    static Stream<Arguments> provideSaveRounds() {
        return Stream.of(
                Arguments.of(
                        game, trainee, round,
                        Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .word("dicht")
                                .wordLength(WordLength.FIVE)
                                .active(true)
                                .turnIds(new ArrayList<>(List.of(new TurnId(1))))
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveRounds")
    @DisplayName("Save a round")
    void test_save_round(Game game, User user, Round round, Round expectedResult) {
        when(mockGameService.findById(game.getGameId())).thenReturn(game);
        when(mockAuthenticationService.getUser()).thenReturn(user);
        when(mockRoundRepository.save(round)).thenReturn(round);

        // Run the test
        final Round result = mockRoundService.saveRound(round);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideCurrentRounds() {
        return Stream.of(
                Arguments.of(
                        1, round,
                        Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .turnIds(new ArrayList<>(List.of(new TurnId(1))))
                                .word("dicht")
                                .wordLength(WordLength.FIVE)
                                .active(true)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCurrentRounds")
    @DisplayName("Find current round")
    void test_find_current_round(int gameId, Round roundResult, Round expectedResult) {
        when(mockRoundRepository.findCurrentRound(gameId)).thenReturn(Optional.ofNullable(roundResult));

        // Run the test
        final Round result = mockRoundService.findCurrentRound(gameId);

        // Verify the results
        assertEquals(expectedResult, result);
    }


    static Stream<Arguments> provideAbsentCurrentRounds() {
        return Stream.of(
                Arguments.of(4),
                Arguments.of(31),
                Arguments.of(8),
                Arguments.of(26)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAbsentCurrentRounds")
    @DisplayName("Find current round without having one")
    void test_find_current_round_without_having_one_should_return_null(int gameId) {
        when(mockRoundRepository.findCurrentRound(gameId)).thenReturn(Optional.empty());

        // Run the test
        final Round result = mockRoundService.findCurrentRound(gameId);

        // Verify the results
        assertNull(result);
    }

    static Stream<Arguments> provideNewRounds() {
        return Stream.of(
                Arguments.of(
                        trainee, 1, round, round2, game, "schoen",
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.SEVEN)
                                .word("alsmaar")
                                .build(),
                        Turn
                                .builder()
                                .turnId(new TurnId(3))
                                .roundId(new RoundId(2))
                                .startedAt(Instant.now())
                                .build(),
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.SEVEN)
                                .word("alsmaar")
                                .build()
                ),
                // without last round
                Arguments.of(
                        trainee, 1, round, null, game, "alsmaar",
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.FIVE)
                                .word("toets")
                                .build(),
                        Turn
                                .builder()
                                .turnId(new TurnId(3))
                                .roundId(new RoundId(2))
                                .startedAt(Instant.now())
                                .build(),
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.FIVE)
                                .word("toets")
                                .build()
                ),
                // without current round
                Arguments.of(
                        trainee, 1, round2, round, game, "toetsen",
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.SEVEN)
                                .word("toetsen")
                                .build(),
                        Turn
                                .builder()
                                .turnId(new TurnId(3))
                                .roundId(new RoundId(2))
                                .startedAt(Instant.now())
                                .build(),
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(1))
                                .wordLength(WordLength.SEVEN)
                                .word("toetsen")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideNewRounds")
    @DisplayName("Create new round")
    void test_create_new_round(User user, int gameId, Round currentRound, Round lastRound,
                               Game game, String randomWord, Round newRound, Turn newTurn, Round expectedResult) {
        when(mockAuthenticationService.getUser()).thenReturn(user);
        when(mockRoundRepository.findCurrentRound(gameId)).thenReturn(Optional.empty());
        when(mockRoundRepository.findLastRound(gameId)).thenReturn(Optional.ofNullable(lastRound));
        when(mockGameService.findById(gameId)).thenReturn(game);
        when(mockDictionaryService.retrieveRandomWord(game.getLanguage(), WordLength.SEVEN)).thenReturn(randomWord);
        when(mockRoundRepository.save(any())).thenReturn(newRound);
        when(mockTurnRepository.save(newTurn)).thenReturn(newTurn);

        // Run the test
        final Round result = mockRoundService.createNewRound(gameId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("provideNewRounds")
    @DisplayName("Create new round when there is another active round")
    void test_create_new_round_with_active_round(User user, int gameId, Round currentRound, Round lastRound,
                               Game game, String randomWord, Round newRound, Turn newTurn, Round voidRound) {
        when(mockAuthenticationService.getUser()).thenReturn(user);
        when(mockRoundRepository.findCurrentRound(gameId)).thenReturn(Optional.ofNullable(currentRound));
        when(mockRoundRepository.findLastRound(gameId)).thenReturn(Optional.ofNullable(lastRound));
        when(mockGameService.findById(gameId)).thenReturn(game);
        when(mockDictionaryService.retrieveRandomWord(game.getLanguage(), WordLength.SEVEN)).thenReturn(randomWord);
        when(mockRoundRepository.save(any())).thenReturn(newRound);
        when(mockTurnRepository.save(newTurn)).thenReturn(newTurn);

        // Verify the results
        assertThrows(DuplicateException.class, () ->  mockRoundService.createNewRound(gameId));
    }

    static Stream<Arguments> provideRoundsForFindRoundById() {
        return Stream.of(
                Arguments.of(1, round,
                        Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .turnIds(new ArrayList<>(List.of(new TurnId(1))))
                                .word("dicht")
                                .wordLength(WordLength.FIVE)
                                .active(true)
                                .build()
                ),
                Arguments.of(2, round2,
                        Round
                                .builder()
                                .roundId(new RoundId(2))
                                .gameId(new GameId(2))
                                .turnIds(new ArrayList<>(List.of(new TurnId(2))))
                                .word("schoen")
                                .wordLength(WordLength.SIX)
                                .active(true)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundsForFindRoundById")
    @DisplayName("Find round by ID")
    void test_find_round_by_id(int roundId, Round roundResult, Round expectedResult) {
        when(mockRoundRepository.findById(roundId)).thenReturn(Optional.ofNullable(roundResult));

        // Run the test
        final Round result = mockRoundService.findRoundById(roundId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Find round by ID which does not exist")
    void test_round_not_found_by_id() {
        when(mockRoundRepository.findById(0)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mockRoundService.findRoundById(0));
    }

    static Stream<Arguments> provideCurrentTurns() {
        return Stream.of(
                Arguments.of(1, turn,
                        Turn
                                .builder()
                                .turnId(new TurnId(1))
                                .roundId(new RoundId(1))
                                .startedAt(currentTime)
                                .build()
                ),
                Arguments.of(2, turn2,
                        Turn
                                .builder()
                                .turnId(new TurnId(2))
                                .roundId(new RoundId(2))
                                .startedAt(currentTime)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCurrentTurns")
    @DisplayName("Find current turn")
    void test_find_current_turn(int roundId, Turn turnResult, Turn expectedResult) {
        when(mockTurnRepository.findCurrentTurn(roundId)).thenReturn(Optional.ofNullable(turnResult));

        final Turn result = mockRoundService.findCurrentTurn(roundId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Current turn not found")
    void test_find_current_turn_not_found() {
        when(mockTurnRepository.findCurrentTurn(0)).thenReturn(Optional.empty());

        // Verify the results
        assertThrows(NotFoundException.class, () -> mockRoundService.findCurrentTurn(0));
    }

    @Test
    @DisplayName("Play turn with no active turn found")
    void test_play_turn_with_no_active_turn_found() {
        when(mockTurnRepository.findCurrentTurn(0)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mockRoundService.playTurn(0, ""));
    }

    static Stream<Arguments> providePlayTurn() {
        Turn playTurn = Turn
                .builder()
                .roundId(new RoundId(1))
                .startedAt(Instant.now())
                .build();
        Turn playTurnTime = Turn
                .builder()
                .roundId(new RoundId(1))
                .startedAt(Instant.now().minusSeconds(11))
                .build();

        return Stream.of(
                Arguments.of(2, 1, 1, trainee, game, round, playTurn, "LEPEL", true, new ArrayList<>(), null), // no status
                Arguments.of(2, 1, 1, trainee, game, round, playTurn, "ASDDS", false, new ArrayList<>(), TurnFeedback.GUESSED_WORD_NOT_FOUND), // word does not exist
                Arguments.of(2, 1, 1, trainee, game, round, playTurn, "schilderij", true, new ArrayList<>(), TurnFeedback.GUESSED_WORD_DIFF_LENGTH), // word not same length
                Arguments.of(2, 1, 1, trainee, game, round, playTurn, "heup!", true, new ArrayList<>(), TurnFeedback.GUESSED_WORD_INVALID_CHAR), // word has invalid characters
                Arguments.of(2, 1, 1, trainee, game, round, playTurnTime, "LEPEL", true, new ArrayList<>(), TurnFeedback.TURN_TIME_OVER) // turn was over time limit
        );
    }

    @ParameterizedTest
    @MethodSource("providePlayTurn")
    @DisplayName("Play turn")
    void test_play_turn(int userId, int gameId, int roundId,
                      User user, Game currentGame, Round currentRound, Turn currentTurn,
                      String guessedWord, boolean wordExists, List<Turn> activeTurns, TurnFeedback expectedResult
    ) {
        when(mockTurnRepository.findCurrentTurn(gameId)).thenReturn(Optional.ofNullable(currentTurn));

        when(mockRoundRepository.findById(roundId)).thenReturn(Optional.ofNullable(currentRound));

        when(mockGameService.findById(gameId)).thenReturn(currentGame);

        when(mockDictionaryService.existsByWord(any(), any())).thenReturn(wordExists);

        when(mockTurnRepository.save(currentTurn)).thenReturn(currentTurn);

        when(mockUserService.findById(userId)).thenReturn(user);

        when(mockTurnRepository.findActiveTurnsByRoundId(roundId)).thenReturn(activeTurns);

        when(mockGameService.save(game)).thenReturn(game);

        when(mockAuthenticationService.getUser()).thenReturn(user);

        when(mockRoundRepository.save(round)).thenReturn(round);

        when(mockUserService.save(user)).thenReturn(user);

        // Run the test
        final Turn result = mockRoundService.playTurn(gameId, guessedWord);

        // Verify the results
        assertEquals(expectedResult, result.getFeedback().getStatus());
    }

    static Stream<Arguments> provideActiveTurnsByRoundId() {
        return Stream.of(
                Arguments.of(1,
                        List.of(turn),
                        List.of(
                                Turn
                                        .builder()
                                        .turnId(new TurnId(1))
                                        .roundId(new RoundId(1))
                                        .startedAt(currentTime)
                                        .build()
                        )
                ),
                Arguments.of(2, new ArrayList<>(), new ArrayList<>())
        );
    }

    @ParameterizedTest
    @MethodSource("provideActiveTurnsByRoundId")
    @DisplayName("Find active turns by round ID")
    void testFindActiveTurnsByRoundId(int roundId, List<Turn> turns, List<Turn> expectedResult) {
        when(mockTurnRepository.findActiveTurnsByRoundId(roundId)).thenReturn(turns);

        // Run the test
        final List<Turn> result = mockRoundService.findActiveTurnsByRoundId(roundId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideSaveTurns() {
        return Stream.of(
                Arguments.of(turn, turn,
                        Turn
                                .builder()
                                .turnId(new TurnId(1))
                                .roundId(new RoundId(1))
                                .startedAt(currentTime)
                                .build()
                ),
                Arguments.of(turn2, turn2,
                        Turn
                                .builder()
                                .turnId(new TurnId(2))
                                .roundId(new RoundId(2))
                                .startedAt(currentTime)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTurns")
    @DisplayName("Save a turn")
    void test_save_turn(Turn turn, Turn turnResult, Turn expectedResult) {
        when(mockTurnRepository.save(turn)).thenReturn(turnResult);

        // Run the test
        final Turn result = mockRoundService.saveTurn(turn);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
