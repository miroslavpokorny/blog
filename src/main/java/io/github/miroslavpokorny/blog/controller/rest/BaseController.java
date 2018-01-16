package io.github.miroslavpokorny.blog.controller.rest;

import io.github.miroslavpokorny.blog.model.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    public ResponseEntity notFoundResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setMessage(message);
        json.setCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(json, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity notFoundResponse() {
        return notFoundResponse("Not found!");
    }

    public ResponseEntity conflictResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setMessage(message);
        json.setCode(HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(json, HttpStatus.CONFLICT);
    }

    public ResponseEntity conflictResponse() {
        return conflictResponse("Conflict!");
    }

    public ResponseEntity badRequestResponse() {
        return badRequestResponse("Bad request!");
    }

    public ResponseEntity badRequestResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setMessage(message);
        json.setCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity internalServerErrorResponse() {
        return internalServerErrorResponse("Internal server error!");
    }

    public ResponseEntity internalServerErrorResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setMessage(message);
        json.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
