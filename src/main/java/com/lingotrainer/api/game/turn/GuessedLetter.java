package com.lingotrainer.api.game.turn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class GuessedLetter {
    private char letter;
    private LetterFeedback letterFeedback;
}
