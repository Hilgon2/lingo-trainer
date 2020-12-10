package com.lingotrainer.infrastructure.persistency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class FileIOException extends RuntimeException {
    public FileIOException(String e) {
        super(e);
    }
}
