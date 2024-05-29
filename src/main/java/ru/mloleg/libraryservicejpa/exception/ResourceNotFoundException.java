package ru.mloleg.libraryservicejpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find resource.")
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException (String message) {
        super(message);
    }
}
