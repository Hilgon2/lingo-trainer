package com.lingotrainer.domain;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static UserId defaultUserId = new UserId(3);

    static Stream<Arguments> provideGames() {
        List<Game> games = new ArrayList<>();
        games.add(Game.builder()
                .gameId(new GameId(2))
                .gameStatus(GameStatus.FINISHED)
                .userId(defaultUserId)
                .build());
        games.add(Game.builder()
                .gameId(new GameId(4))
                .gameStatus(GameStatus.ACTIVE)
                .userId(defaultUserId)
                .build());
        games.add(Game.builder()
                .gameId(new GameId(7))
                .userId(defaultUserId)
                .gameStatus(GameStatus.FINISHED)
                .build());
        games.add(Game.builder()
                .gameId(new GameId(11))
                .userId(defaultUserId)
                .gameStatus(GameStatus.FINISHED)
                .build());

        return Stream.of(
                Arguments.of(games));
    }

    @ParameterizedTest
    @MethodSource("provideGames")
    @DisplayName("Has a maximum of 1 active game")
    void test_has_zero_or_one_active_game(List<Game> games) {
        assertTrue(
                games.stream()
                        .filter(game -> game.getGameStatus() == GameStatus.ACTIVE && game.getUserId() == defaultUserId.getId())
                        .count() <= 1
        );
    }
}
