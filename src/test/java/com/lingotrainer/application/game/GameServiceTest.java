package com.lingotrainer.application.game;

import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.domain.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GameServiceTest {

    @Mock
    private GameRepository mockGameRepository;
    @Mock
    private AuthenticationService mockAuthenticationService;

    private BaseGameService mockGameService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        mockGameService = new BaseGameService(mockGameRepository, mockAuthenticationService);
    }

    static Stream<Arguments> provideFindGameById() {
        return Stream.of(
                Arguments.of(Game
                                .builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build(),
                        Game
                                .builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build()
                ));
    }

    @ParameterizedTest
    @MethodSource("provideFindGameById")
    @DisplayName("Find a game by it's ID")
    void test_find_game_by_id(Game gameResult, Game expectedResult) {
        when(mockGameRepository.findById(0)).thenReturn(Optional.ofNullable(gameResult));

        // Run the test
        Game result = mockGameService.findById(0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Game should not be able to be found")
    void test_find_game_by_id_return_absent() {
        when(mockGameRepository.findById(0)).thenReturn(Optional.empty());

        // Verify the results
        assertThrows(NotFoundException.class, () -> mockGameService.findById(0));
    }

    static Stream<Arguments> provideNewGameCreation() {
        return Stream.of(
                Arguments.of(
                        Game.builder()
                                .gameId(new GameId(1))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(new ArrayList<>())
                                .gameStatus(GameStatus.ACTIVE)
                                .build(),
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("password")
                                .highscore(0)
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        Game.builder()
                                .gameId(new GameId(1))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(new ArrayList<>())
                                .gameStatus(GameStatus.ACTIVE)
                                .build()
                ));
    }

    @ParameterizedTest
    @MethodSource("provideNewGameCreation")
    @DisplayName("Create a new game")
    void test_create_new_game(Game expectedResult, User user, Game newGame) {
        // Setup
        when(mockGameRepository.hasActiveGame(0)).thenReturn(false);
        when(mockAuthenticationService.getUser()).thenReturn(user);
        when(mockGameRepository.save(any())).thenReturn(newGame);
        when(mockGameService.save(newGame)).thenReturn(newGame);

        // Run the test
        final Game result = mockGameService.createNewGame(newGame.getLanguage());

        // Verify the results
        assertFalse(mockGameService.hasActiveGame(user.getUserId()));
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideActiveGame() {
        return Stream.of(
                Arguments.of(
                        Game.builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build(),
                        Game.builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideActiveGame")
    @DisplayName("User should have an active game")
    void test_user_should_have_an_active_game(Game expectedResult, Game game) {
        when(mockGameRepository.findActiveGame(0)).thenReturn(Optional.of(game));

        // Run the test
        final Game result = mockGameService.findActiveGame(0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideNonActiveGames() {
        return Stream.of(
                Arguments.of(new GameId(3), new UserId(1)),
                Arguments.of(new GameId(4), new UserId(1)),
                Arguments.of(new GameId(5), new UserId(1))
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonActiveGames")
    @DisplayName("System should not return an active game")
    void test_user_should_not_have_an_active_game(GameId searchForGameId, UserId userId) {
        when(mockGameRepository.findActiveGame(userId.getId())).thenReturn(Optional.empty());

        // Run the test
        final Game result = mockGameService.findActiveGame(searchForGameId.getId());

        // Verify the results
        assertNull(result);
    }

    static Stream<Arguments> provideSaveGames() {
        return Stream.of(
                Arguments.of(
                        Game.builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build(),
                        Game.builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build(),
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("password")
                                .highscore(0)
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        Game.builder()
                                .gameId(new GameId(0))
                                .userId(new UserId(1))
                                .score(0)
                                .language("nl_nl")
                                .roundIds(List.of(new RoundId(0)))
                                .gameStatus(GameStatus.ACTIVE)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveGames")
    @DisplayName("Save a game")
    void test_save_game(Game newGame, Game expectedResult, User user, Game repositorySave) {
        when(mockAuthenticationService.getUser()).thenReturn(user);
        when(mockGameRepository.save(newGame)).thenReturn(newGame);

        // Run the test
        final Game result = mockGameService.save(repositorySave);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
