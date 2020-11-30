package com.lingotrainer.domain.model.game.round.turn;

import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.game.GameFeedback;
import com.lingotrainer.domain.model.game.round.RoundId;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Turn {
    private TurnId turnId;
    private String guessedWord;

    private List<GuessedLetter> guessedLetters;

    private boolean correctGuess;

    private Map<String, Object> feedback = new HashMap<>();

    private Instant startedAt;

    @ToString.Exclude
    private RoundId roundId;

    public void setGuessedLetters(String answer) {
        if (
                this.guessedWord.length() == answer.length()
                && (this.getFeedback().get("status") == null
                || Boolean.parseBoolean(this.getFeedback().get("gameOver").toString()))
        ) {
            int index = 0;
            this.guessedLetters = new ArrayList<>();

            if (this.guessedWord != null) {
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
    }

    public void validateTurn(String answer, Dictionary dictionary) {
        this.feedback = new HashMap<>();
        // feedback code -9999 means there is no feedback, meaning there is no error.
        int feedbackCode = -9999;
        GameFeedback status = null;

        if (this.guessedWord == null) {
            feedbackCode = 5200;
            this.setGuessedWord("-");
            status = GameFeedback.GUESSED_WORD_IS_NULL;
        }

        // Trim and capitalize the guessed word.
        // Do this after the null check, because a null does not have a trim or toUpperCase method.
        // This could otherwise possibly cause an error.
        this.guessedWord = guessedWord.toUpperCase().trim();

        if (!dictionary.getWords().contains(this.getGuessedWord())) {
            feedbackCode = 5200;
            status = GameFeedback.GUESSED_WORD_NOT_FOUND;
        } else if (answer.length() != this.getGuessedWord().length()) {
            feedbackCode = 5205;
            status = GameFeedback.GUESSED_WORD_DIFF_LENGTH;
        } else if (!this.getGuessedWord().chars().allMatch(Character::isLetter)) {
            feedbackCode = 5210;
            status = GameFeedback.GUESSED_WORD_INVALID_CHAR;
            // TODO: change 1500 to 10 (seconds)
        } else if (Duration.between(this.getStartedAt(), Instant.now()).getSeconds() > 1500) {
            feedbackCode = 5215;
            status = GameFeedback.TURN_OVER;
        }

        this.correctGuess = this.guessedWord.equalsIgnoreCase(answer);
        this.feedback.put("code", feedbackCode);
        this.feedback.put("status", status);
        this.feedback.put("gameOver", false);
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

    public void finishGame() {
        feedback.put("code", 5001);
        feedback.put("status", GameFeedback.GAME_OVER);
        feedback.put("gameOver", true);
    }
}
