package com.lingotrainer.model.game;

import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.user.UserId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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
    void has_zero_or_one_active_game() {
        assertTrue(
                this.games.stream()
                        .filter(game -> game.getGameStatus() == GameStatus.ACTIVE
                                && game.getUserId() == this.defaultUserId.getId())
                        .count() <= 1
        );
    }
}
