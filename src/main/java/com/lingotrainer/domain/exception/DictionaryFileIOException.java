package com.lingotrainer.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class DictionaryFileIOException extends RuntimeException {
    public DictionaryFileIOException(String e) {
        super(e);
    }
}
