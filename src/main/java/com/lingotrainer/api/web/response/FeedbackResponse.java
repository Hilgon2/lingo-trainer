package com.lingotrainer.api.web.response;

import com.lingotrainer.domain.model.game.round.turn.TurnFeedback;
import com.lingotrainer.domain.model.game.round.turn.GuessedLetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class FeedbackResponse {
    private List<GuessedLetter> guessedLetters;
    private int code;
    private TurnFeedback status;
}
