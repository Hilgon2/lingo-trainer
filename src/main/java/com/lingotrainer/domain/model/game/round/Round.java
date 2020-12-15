package com.lingotrainer.domain.model.game.round;

import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
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
        if (this.wordLength != null) {
            return this.wordLength;
        } else if (this.word == null) {
            return WordLength.FIVE;
        } else {
            switch (this.word.length()) {
                case 5:
                    return WordLength.FIVE;
                case 6:
                    return WordLength.SIX;
                case 7:
                    return WordLength.SEVEN;
                default:
                    return null;
            }
        }
    }

    public WordLength retrieveNextWordLength(Round lastRound) {
        if (lastRound != null) {
            switch (lastRound.getWordLength()) {
                case FIVE:
                    this.wordLength = WordLength.SIX;
                    break;
                case SIX:
                    this.wordLength = WordLength.SEVEN;
                    break;
                default:
                    this.wordLength = WordLength.FIVE;
                    break;
            }
        } else {
            this.wordLength = WordLength.FIVE;
        }

        return this.wordLength;
    }

    public void checkActiveTurns(List<Turn> turns) {
        if (turns.size() > 0) {
            throw new GameException(
                    "There are still turns left on the current round. "
                            + "Please finish them before creating a new round."
            );
        }
    }
}
