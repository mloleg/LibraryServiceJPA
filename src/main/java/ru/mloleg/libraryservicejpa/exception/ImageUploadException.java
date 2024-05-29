package ru.mloleg.libraryservicejpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error during image upload.")
public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message) {
        super(message);
    }
}
