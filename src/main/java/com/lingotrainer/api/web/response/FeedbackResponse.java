package com.lingotrainer.api.web.response;

import com.lingotrainer.domain.model.game.GameFeedback;
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
    List<GuessedLetter> guessedLetters;
    int code;
    GameFeedback status;
}
