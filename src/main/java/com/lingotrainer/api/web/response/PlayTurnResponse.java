package com.lingotrainer.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class PlayTurnResponse {
    private String guessedWord;
    private FeedbackResponse feedback;
    private boolean gameOver;
    private boolean correctGuess;
}
