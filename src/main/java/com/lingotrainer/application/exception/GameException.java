package com.lingotrainer.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameException extends RuntimeException {

    public GameException(String value) {
        super(value);
    }

}