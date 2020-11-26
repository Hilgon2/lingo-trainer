package com.lingotrainer.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        this("The server understood the request, but is refusing action. Check if you have the necessary permissions.");
    }

    public ForbiddenException(String e) {
        super(e);
    }
}
