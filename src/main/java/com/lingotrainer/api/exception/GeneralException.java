package com.lingotrainer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class GeneralException extends RuntimeException {

    public GeneralException() {
    }

    public GeneralException(String value) {
        super(value);
    }

}