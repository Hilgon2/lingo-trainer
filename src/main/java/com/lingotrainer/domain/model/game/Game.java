package com.lingotrainer.domain.model.game;

import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.user.UserId;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private GameId gameId;

    private UserId userId;

    private int score;

    private String language;

    @ToString.Exclude
    private List<RoundId> roundIds = new ArrayList<>();

    private GameStatus gameStatus;

    public int getGameId() {
        if (this.gameId == null) {
            return 0;
        }
        return this.gameId.getId();
    }

    public int getUserId() {
        return this.userId.getId();
    }
}
