package com.lingotrainer.api.domain.model.game.round;

import com.lingotrainer.api.domain.model.game.GameId;
import com.lingotrainer.api.domain.model.game.round.turn.TurnId;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Round {
    private RoundId roundId;

    @ToString.Exclude
    private String word;

    @ToString.Exclude
    private List<TurnId> turnIds = new ArrayList<>();

    private int lettersCount;

    private GameId gameId;

    private boolean active = true;

    public int getLettersCount() {
        return this.word.length();
    }

    public int getRoundId() {
        if (this.roundId == null) {
            return 0;
        }

        return this.roundId.getId();
    }

    public int getGameId() {
        if (this.gameId == null) {
            return 0;
        }

        return this.gameId.getId();
    }

    public void addTurnId(TurnId turnId) {
        this.turnIds.add(turnId);
    }
}
