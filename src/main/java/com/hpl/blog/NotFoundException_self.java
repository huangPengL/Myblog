package com.hpl.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException_self extends RuntimeException{
    public NotFoundException_self() {
    }

    public NotFoundException_self(String message) {
        super(message);
    }

    public NotFoundException_self(String message, Throwable cause) {
        super(message, cause);
    }
}
