package com.lingotrainer.infrastructure.persistency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends RuntimeException {
    public LanguageNotFoundException(String e) {
        super(e);
    }
}
