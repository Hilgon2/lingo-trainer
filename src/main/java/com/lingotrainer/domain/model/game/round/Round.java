package com.lingotrainer.domain.model.game.round;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
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

    private WordLength wordLength;

    public int getLettersCount() {
        if (this.word == null) {
            return 0;
        }

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

    public WordLength getWordLength() {
        if (this.word == null) {
            return WordLength.FIVE;
        }

        switch (this.word.length()) {
            case 5:
                return WordLength.SIX;
            case 6:
                return WordLength.SEVEN;
            case 7:
            default:
                return WordLength.FIVE;
        }
    }

    public void nextWord(Round lastRound, String word) {
        this.nextWordLength(lastRound);
        this.word = word;
    }

    private void nextWordLength(Round lastRound) {
        if (lastRound != null) {
            switch (lastRound.getWordLength()) {
                case SIX:
                    this.wordLength = WordLength.SIX;
                    break;
                case SEVEN:
                    this.wordLength = WordLength.SEVEN;
                    break;
                default:
                    this.wordLength = WordLength.FIVE;
                    break;
            }
        } else {
            this.wordLength = WordLength.FIVE;
        }
    }
}
