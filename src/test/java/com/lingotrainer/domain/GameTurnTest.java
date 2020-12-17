package com.lingotrainer.domain;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.*;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.*;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class GameTurnTest {
    private static GameTurn mockGameTurn;
    private static GameTurn mockGameTurn2;
    private static GameTurn mockGameTurn3;

    private static Instant currentTime = Instant.now();

    @BeforeAll
    static void init() {
        User mockUser = User
                .builder()
                .userId(new UserId(1))
                .username("username")
                .password("wachtwoord123")
                .role(Role.TRAINEE)
                .active(true)
                .gameIds(new ArrayList<>())
                .build();

        Feedback mockFeedback = new Feedback("SCHOOL", "SCHOOL", true, currentTime);

        Turn mockTurn = Turn
                .builder()
                .turnId(new TurnId(1))
                .roundId(new RoundId(1))
                .guessedWord("SCHOOL")
                .startedAt(currentTime)
                .feedback(mockFeedback)
                .build();

        Round mockRound = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .turnIds(List.of(new TurnId(1)))
                .word("SCHOOL")
                .lettersCount(6)
                .wordLength(WordLength.SIX)
                .active(true)
                .build();

        Game mockGame = Game
                .builder()
                .gameId(new GameId(1))
                .roundIds(List.of(new RoundId(1)))
                .userId(new UserId(1))
                .gameStatus(GameStatus.ACTIVE)
                .score(0)
                .language("test-nl_nl")
                .build();

        mockGameTurn = GameTurn
                .builder()
                .user(mockUser)
                .turn(mockTurn)
                .round(mockRound)
                .game(mockGame)
                .activeTurns(new ArrayList<>())
                .build();

        Feedback mockFeedback2 = new Feedback("SLOOP", "SWING", true, currentTime);

        Turn mockTurn2 = Turn
                .builder()
                .turnId(new TurnId(2))
                .roundId(new RoundId(2))
                .guessedWord("SWING")
                .startedAt(currentTime)
                .feedback(mockFeedback2)
                .build();

        Round mockRound2 = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .turnIds(List.of(new TurnId(1)))
                .word("SLOOP")
                .lettersCount(5)
                .wordLength(WordLength.FIVE)
                .active(true)
                .build();

        mockGameTurn2 = GameTurn
                .builder()
                .user(mockUser)
                .turn(mockTurn2)
                .round(mockRound2)
                .game(mockGame)
                .activeTurns(new ArrayList<>(List.of(new Turn(), new Turn(), new Turn(), new Turn(), new Turn())))
                .build();

        Feedback mockFeedback3 = new Feedback("SLOOP", "SWING", true, currentTime);

        Turn mockTurn3 = Turn
                .builder()
                .turnId(new TurnId(2))
                .roundId(new RoundId(2))
                .guessedWord("SWING")
                .startedAt(currentTime)
                .feedback(mockFeedback2)
                .build();

        Round mockRound3 = Round
                .builder()
                .roundId(new RoundId(1))
                .gameId(new GameId(1))
                .turnIds(List.of(new TurnId(1)))
                .word("SLOOP")
                .lettersCount(5)
                .wordLength(WordLength.FIVE)
                .active(true)
                .build();

        mockGameTurn3 = GameTurn
                .builder()
                .user(mockUser)
                .turn(mockTurn3)
                .round(mockRound3)
                .game(mockGame)
                .activeTurns(new ArrayList<>(List.of(new Turn(), new Turn())))
                .build();
    }


    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    static Stream<Arguments> provideGameTurns() {
        return Stream.of(
                Arguments.of(mockGameTurn, GameTurnFeedback.WORD_CORRECT),
                Arguments.of(mockGameTurn2, GameTurnFeedback.WORD_WRONG_NO_TURNS_LEFT),
                Arguments.of(mockGameTurn3, GameTurnFeedback.WORD_WRONG_NEW_TURN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGameTurns")
    @DisplayName("Perform the turn")
    void test_perform_turn(GameTurn gameTurn, GameTurnFeedback expectedResult) {
        // Run the test
        gameTurn.performTurn();

        // Verify the results
        assertEquals(expectedResult, gameTurn.getGameTurnFeedback());
    }
}
