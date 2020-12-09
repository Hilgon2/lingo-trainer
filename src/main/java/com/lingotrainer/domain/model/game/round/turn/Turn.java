package com.lingotrainer.domain.model.game.round.turn;

import com.lingotrainer.domain.model.game.round.RoundId;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Turn {
    private TurnId turnId;
    private String guessedWord;

    private Feedback feedback;

    private Instant startedAt;

    @ToString.Exclude
    private RoundId roundId;

    public void validate(String answer, boolean wordExists) {
        this.feedback = new Feedback(answer, this.guessedWord, wordExists, this.startedAt);
    }

    public int getTurnId() {
        if (this.turnId == null) {
            return 0;
        }

        return this.turnId.getId();
    }

    public int getRoundId() {
        if (this.roundId == null) {
            return 0;
        }

        return this.roundId.getId();
    }

    public void setGuessedWord(String guessedWord) {
        if (guessedWord != null) {
            this.guessedWord = guessedWord.toUpperCase();
        }
    }
}
