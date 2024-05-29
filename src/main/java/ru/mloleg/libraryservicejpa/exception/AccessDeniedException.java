package ru.mloleg.libraryservicejpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You should be logged in.")
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super();
    }

}
