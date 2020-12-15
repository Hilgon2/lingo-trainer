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
        this.defaultValidation();

        if (this.guessedWord != null && this.status == null) {
            this.correctGuess = this.guessedWord.equalsIgnoreCase(answer);
        }

        this.setGuessedLetters();
    }

    public void setGuessedLetters() {
        if (this.code == -9999 && this.status == null) {

            char[] answerChars = this.answer.toCharArray();
            char[] guessedChars = this.guessedWord.toCharArray();
            List<GuessedLetter> newGuessedLetters = new ArrayList<>();

            // default all absent
            for (char letter : guessedChars) {
                newGuessedLetters.add(GuessedLetter
                        .builder()
                        .letter(letter)
                        .letterFeedback(LetterFeedback.ABSENT)
                        .build());
            }

            // if whole word is correct, set all on correct
            if (this.correctGuess) {
                newGuessedLetters.forEach(guessedLetter ->
                        guessedLetter.setLetterFeedback(LetterFeedback.CORRECT)
                );
            } else {
                // check for the correct letters
                // if letter is correct, change characters to a hyphen.
                // This way we can tell not to check for the char again later on
                for (int guessCount = 0; guessCount < guessedChars.length; guessCount++) {
                    if (guessedChars[guessCount] == answerChars[guessCount]) {
                        newGuessedLetters.get(guessCount).setLetterFeedback(LetterFeedback.CORRECT);
                        guessedChars[guessCount] = '-';
                        answerChars[guessCount] = '-';
                    }
                }

                // first loop through all letters of the guessed word
                // then only go further if the current char is not a hyphen
                // then loop through all letters of the answer. Only check characters which are not a hyphen. Then check
                // if current answer loop is the same as the current guessed character, if so; mark them as PRESENT.
                for (int guessCount = 0; guessCount < guessedChars.length; guessCount++) {
                    if (guessedChars[guessCount] != '-') {
                        for (int answerCount = 0; answerCount < answerChars.length; answerCount++) {
                            if (answerChars[answerCount] != '-'
                                    && answerChars[answerCount] == guessedChars[guessCount]) {
                                newGuessedLetters.get(guessCount)
                                        .setLetterFeedback(LetterFeedback.PRESENT);
                                guessedChars[guessCount] = '-';
                                answerChars[answerCount] = '-';
                            }
                        }
                    }
                }
            }

            this.guessedLetters = newGuessedLetters;
        }
    }

    public void defaultValidation() {
        if (this.guessedWord == null) {
            this.code = 5200;
            this.setGuessedWord("-");
            status = TurnFeedback.GUESSED_WORD_IS_NULL;
        } else {

            // Trim and capitalize the guessed word.
            // Do this after the null check, because a null does not have a trim or toUpperCase method.
            // This could otherwise possibly cause an error.
            this.guessedWord = guessedWord.toUpperCase().trim();

            if (Duration.between(this.getStartedAt(), Instant.now()).getSeconds() > 10) {
                this.code = 5220;
                this.status = TurnFeedback.TURN_TIME_OVER;
            } else if (answer.length() != this.getGuessedWord().length()) {
                this.code = 5205;
                this.status = TurnFeedback.GUESSED_WORD_DIFF_LENGTH;
            } else if (!this.getGuessedWord().chars().allMatch(Character::isLetter)) {
                this.code = 5210;
                this.status = TurnFeedback.GUESSED_WORD_INVALID_CHAR;
            } else if (!wordExists) {
                this.code = 5215;
                this.status = TurnFeedback.GUESSED_WORD_NOT_FOUND;
                // TODO: change 1500 to 10 (seconds)
            }
        }
    }
}
