package com.lingotrainer.infrastructure.persistency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class LanguageNotFoundException extends RuntimeException {
    public LanguageNotFoundException(String e) {
        super(e);
    }
}
