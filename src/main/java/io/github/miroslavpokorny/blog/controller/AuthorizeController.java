package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.model.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AuthorizeController extends BaseController {
    public ResponseEntity unAuthorizedResponse() {
        return unAuthorizedResponse("UNAUTHORIZED or low permission role!");
    }

    public ResponseEntity unAuthorizedResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setCode(HttpStatus.UNAUTHORIZED.value());
        json.setMessage(message);
        return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity forbiddenResponse() {
        return forbiddenResponse("FORBIDDEN");
    }

    public ResponseEntity forbiddenResponse(String message) {
        ErrorMessageDto json = new ErrorMessageDto();
        json.setCode(HttpStatus.FORBIDDEN.value());
        json.setMessage(message);
        return new ResponseEntity<>(json, HttpStatus.FORBIDDEN);
    }
}
