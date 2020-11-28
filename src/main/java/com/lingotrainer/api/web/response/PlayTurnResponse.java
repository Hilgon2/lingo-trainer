package com.lingotrainer.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class PlayTurnResponse {
    private String guessedWord;
    private List<GuessedLetterResponse> guessedLetters;
    private boolean correctGuess;
    private Map<String, Object> feedback;
}
