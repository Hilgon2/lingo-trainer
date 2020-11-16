package com.lingotrainer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class GameException extends RuntimeException {

    public GameException() {
    }

    public GameException(String value) {
        super(value);
    }

}