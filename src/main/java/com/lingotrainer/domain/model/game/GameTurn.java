package com.lingotrainer.domain.model.game;

import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import com.lingotrainer.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameTurn {
    private User user;
    private Turn turn;
    private Round round;
    private Game game;
    private List<TurnId> activeTurns;
    private Turn newTurn;
    private GameTurnFeedback gameTurnFeedback;

    public void performTurn() {
        if (turn.getFeedback().isCorrectGuess()) {
            game.setScore(game.getScore() + 1);
            this.round.setActive(false);
            this.gameTurnFeedback = GameTurnFeedback.WORD_CORRECT;
        } else if (!turn.getFeedback().isCorrectGuess()
                && activeTurns.size() >= 5) {
            this.turn.getFeedback().setGameOver(true);
            this.round.setActive(false);
            this.gameTurnFeedback = GameTurnFeedback.WORD_WRONG_NO_TURNS_LEFT;
        } else {
            this.newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();
            this.gameTurnFeedback = GameTurnFeedback.WORD_WRONG_NEW_TURN;
        }
    }
}
