package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.GuessedLetterResponse;
import com.lingotrainer.api.web.response.LetterFeedbackResponse;
import com.lingotrainer.api.web.response.PlayTurnResponse;
import com.lingotrainer.domain.model.game.round.turn.GuessedLetter;
import com.lingotrainer.domain.model.game.round.turn.LetterFeedback;
import com.lingotrainer.domain.model.game.round.turn.Turn;

import java.util.ArrayList;
import java.util.List;

public class TurnFormMapper {
    public PlayTurnResponse convertToResponse(Turn turn) {
        PlayTurnResponse playTurnResponse = PlayTurnResponse.builder()
                .correctGuess(turn.isCorrectGuess())
                .feedback(turn.getFeedback())
                .guessedWord(turn.getGuessedWord())
                .build();

        if (turn.getGuessedLetters() != null) {
            playTurnResponse.setGuessedLetters(this.convertToGuessedLettersResponse(turn.getGuessedLetters()));
        }

        return playTurnResponse;
    }

    public List<GuessedLetterResponse> convertToGuessedLettersResponse(List<GuessedLetter> guessedLetters) {
        List<GuessedLetterResponse> guessedLetterResponse = new ArrayList<>();
        guessedLetters
                .forEach(guessedLetter -> guessedLetterResponse.add(
                        GuessedLetterResponse.builder()
                                .letter(guessedLetter.getLetter())
                                .letterFeedback(this.convertToLetterFeedbackResponse(guessedLetter.getLetterFeedback()))
                                .build()));

        return guessedLetterResponse;
    }

    public LetterFeedbackResponse convertToLetterFeedbackResponse(LetterFeedback letterFeedback) {
        LetterFeedbackResponse letterFeedbackResponse;
        switch (letterFeedback) {
            case ABSENT:
                letterFeedbackResponse = LetterFeedbackResponse.ABSENT;
                break;
            case CORRECT:
                letterFeedbackResponse = LetterFeedbackResponse.CORRECT;
                break;
            case PRESENT:
                letterFeedbackResponse = LetterFeedbackResponse.PRESENT;
                break;
            default:
                letterFeedbackResponse = LetterFeedbackResponse.ABSENT;
                break;

        }
        return letterFeedbackResponse;

    }
}
