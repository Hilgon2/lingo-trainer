package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.FeedbackResponse;
import com.lingotrainer.api.web.response.PlayTurnResponse;
import com.lingotrainer.domain.model.game.round.turn.Feedback;
import com.lingotrainer.domain.model.game.round.turn.Turn;

public class TurnFormMapper {
    public PlayTurnResponse convertToResponse(Turn turn) {
        return PlayTurnResponse.builder()
                .feedback(this.convertToFeedbackResponse(turn.getFeedback()))
                .guessedWord(turn.getGuessedWord())
                .gameOver(turn.getFeedback().isGameOver())
                .build();
    }

    public FeedbackResponse convertToFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .code(feedback.getCode())
                .guessedLetters(feedback.getGuessedLetters())
                .status(feedback.getStatus())
                .build();
    }
}
