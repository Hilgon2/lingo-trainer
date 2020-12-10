package com.lingotrainer.domain;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    private List<Game> games;
    private UserId defaultUserId = new UserId(3);

    @BeforeEach
    void setup() {
        this.games = new ArrayList<>();
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
    }

    @Test
    @DisplayName("Has zero or one active game")
    void hasZeroOrOneActiveGame() {
        assertTrue(
                this.games.stream()
                        .filter(game -> game.getGameStatus() == GameStatus.ACTIVE
                                && game.getUserId() == this.defaultUserId.getId())
                        .count() <= 1
        );
    }
}
