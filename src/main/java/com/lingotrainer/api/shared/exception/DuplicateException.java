package com.lingotrainer.api.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class DuplicateException extends RuntimeException {

    public DuplicateException(String value) {
        super(value);
    }

}