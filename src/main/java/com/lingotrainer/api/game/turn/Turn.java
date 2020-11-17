package com.lingotrainer.api.game.turn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingotrainer.api.game.Game;
import com.lingotrainer.api.game.round.Round;
import com.lingotrainer.api.game.GameFeedback;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "turns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="guessed_word")
    private String guessedWord;

    @Transient
    private List<GuessedLetter> guessedLetters;

    @Transient
    private boolean correctGuess;

    @Transient
    private Map<String, Object> feedback = new HashMap<>();

    @Column(name="started_at")
    private Instant startedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private Round round;

    public Turn validateTurn() {
        Round round = this.getRound();
        Game game = round.getGame();

        // feedback code -999 means there is no feedback, meaning there is no error.
        int feedbackCode = -9999;
        GameFeedback status = null;

        if (game.getGameStatus() != Game.GameStatus.ACTIVE) {
            feedbackCode = 5000;
            status = GameFeedback.NO_ACTIVE_GAME;
        }

        if (round.getTurns()
                .stream()
                .filter(t -> t.getGuessedWord() != null)
                .count() >= 5) {
            feedbackCode = 5105;
            status = GameFeedback.NO_TURNS_LEFT;
        }

        if (this.guessedWord == null) {
            feedbackCode = 5200;
            this.setGuessedWord("-");
            status = GameFeedback.GUESSED_WORD_IS_NULL;
        }

        if (round.getWord().length() != this.getGuessedWord().length()) {
            feedbackCode = 5205;
            status = GameFeedback.GUESSED_WORD_DIFF_LENGTH;
        }

        if (!this.getGuessedWord().chars().allMatch(Character::isLetter)) {
            feedbackCode = 5210;
            status = GameFeedback.GUESSED_WORD_INVALID_CHAR;
        }

        if (Duration.between(this.getStartedAt(), Instant.now()).getSeconds() > 1500) { // TODO: change 1500 to 10 (seconds)
            feedbackCode = 5215;
            status = GameFeedback.TURN_OVER;
        }
        
        this.feedback.put("code", feedbackCode);
        this.feedback.put("status", status);
        
        return this;
    }
}
