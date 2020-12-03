package com.lingotrainer.domain.model.game.round.turn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Feedback {
    @ToString.Exclude
    @JsonIgnore
    private String answer;

    @ToString.Exclude
    @JsonIgnore
    private String guessedWord;

    private List<GuessedLetter> guessedLetters;
    // feedback code -9999 means there is no feedback, meaning there is no error.
    private int code = -9999;
    private TurnFeedback status;
    private boolean correctGuess;
    private boolean gameOver;
    private boolean wordExists;
    private Instant startedAt;

    public Feedback(String answer, String guessedWord, boolean wordExists, Instant startedAt) {
        this.answer = answer;
        this.guessedWord = guessedWord;
        this.wordExists = wordExists;
        this.startedAt = startedAt;

        this.doValidation();
    }

    public void doValidation() {
        this.setGuessedLetters();
        this.defaultValidation();
    }

    public void setGuessedLetters() {
        if (
                this.guessedWord.length() == this.answer.length()
                        && (this.status == null || this.gameOver)
        ) {
            int index = 0;
            this.guessedLetters = new ArrayList<>();

            for (char letter : this.guessedWord.toCharArray()) {
                LetterFeedback letterFeedback;
                if (letter == Character.toUpperCase(answer.charAt(index))) {
                    letterFeedback = LetterFeedback.CORRECT;
                } else if (answer.toUpperCase().indexOf(letter) != -1) {
                    letterFeedback = LetterFeedback.PRESENT;
                } else {
                    letterFeedback = LetterFeedback.ABSENT;
                }
                this.guessedLetters.add(GuessedLetter.builder()
                        .letter(letter)
                        .letterFeedback(letterFeedback)
                        .build());
                index++;
            }
        }
    }

    public void defaultValidation() {
        if (this.guessedWord == null) {
            this.code = 5200;
            this.setGuessedWord("-");
            status = TurnFeedback.GUESSED_WORD_IS_NULL;
        }

        // Trim and capitalize the guessed word.
        // Do this after the null check, because a null does not have a trim or toUpperCase method.
        // This could otherwise possibly cause an error.
        this.guessedWord = guessedWord.toUpperCase().trim();

        if (!wordExists) {
            this.code = 5200;
            this.status = TurnFeedback.GUESSED_WORD_NOT_FOUND;
        } else if (answer.length() != this.getGuessedWord().length()) {
            this.code = 5205;
            this.status = TurnFeedback.GUESSED_WORD_DIFF_LENGTH;
        } else if (!this.getGuessedWord().chars().allMatch(Character::isLetter)) {
            this.code = 5210;
            this.status = TurnFeedback.GUESSED_WORD_INVALID_CHAR;
            // TODO: change 1500 to 10 (seconds)
        } else if (Duration.between(this.getStartedAt(), Instant.now()).getSeconds() > 1500) {
            this.code = 5215;
            this.status = TurnFeedback.TURN_TIME_OVER;
        }

        this.correctGuess = this.guessedWord.equalsIgnoreCase(answer);
    }
}
