package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.model.json.ErrorMessageJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AuthorizeController {
    public ResponseEntity unAuthorizedResponse() {
        return unAuthorizedResponse("UNAUTHORIZED or low permission role!");
    }

    public ResponseEntity unAuthorizedResponse(String message) {
        ErrorMessageJson json = new ErrorMessageJson();
        json.setCode(HttpStatus.UNAUTHORIZED.value());
        json.setMessage(message);
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
