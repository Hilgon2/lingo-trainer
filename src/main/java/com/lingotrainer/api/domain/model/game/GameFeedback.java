package com.lingotrainer.api.domain.model.game;

public enum GameFeedback {
    NO_ACTIVE_GAME,
    GAME_OVER,

    NO_TURNS_LEFT,
    TURN_OVER,

    GUESSED_WORD_IS_NULL,
    GUESSED_WORD_DIFF_LENGTH,
    GUESSED_WORD_INVALID_CHAR,
    GUESSED_WORD_NOT_FOUND
}
