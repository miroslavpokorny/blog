package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.model.json.ErrorMessageJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    public ResponseEntity notFoundResponse(String message) {
        ErrorMessageJson json = new ErrorMessageJson();
        json.setMessage(message);
        json.setCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(json, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity notFoundResponse() {
        return notFoundResponse("Not found!");
    }
}
