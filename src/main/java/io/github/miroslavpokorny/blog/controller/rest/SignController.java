package io.github.miroslavpokorny.blog.controller.rest;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.dto.LoggedUserDto;
import io.github.miroslavpokorny.blog.model.error.EmailAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.error.NicknameAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.helper.validation.Validator;
import io.github.miroslavpokorny.blog.model.dto.ErrorMessageDto;
import io.github.miroslavpokorny.blog.model.dto.SignInCredentialsDto;
import io.github.miroslavpokorny.blog.model.dto.SignUpDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SignController extends BaseController{
    private final Authentication authentication;

    @Autowired
    public SignController(Authentication authentication) {
        this.authentication = authentication;
    }

    @RequestMapping(value = "/api/sign/in", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestBody SignInCredentialsDto credentials, @RequestParam(value = "tokenId", required = false) String tokenId) {
        if (tokenId != null) {
            authentication.destroyAuthentication(tokenId);
        }
        if (credentials == null || credentials.getEmail() == null || credentials.getPassword() == null) {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setCode(400);
            json.setMessage("Bad data format, please try reload page and try again.");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        String newTokenId = authentication.createAuthentication(credentials.getEmail(), credentials.getPassword());
        if (newTokenId != null) {
            LoggedUserDto json = getLoggedUserJson(newTokenId);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setMessage("Wrong email or password!");
            json.setCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/api/sign/out")
    public ResponseEntity signOut(@RequestParam(value = "tokenId", required = false) String tokenId) {
        if (tokenId != null) {
            authentication.destroyAuthentication(tokenId);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/api/getLoggedUser")
    public ResponseEntity getLoggedUser(@RequestParam(value = "tokenId", required = false) String tokenId) {
        if (authentication.isAuthenticate(tokenId)) {
            LoggedUserDto json = getLoggedUserJson(tokenId);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping("/api/sign/up")
    public ResponseEntity isSigned(@RequestBody SignUpDataDto signUpData, @RequestParam(value = "tokenId", required = false) String tokenId) {
        if (tokenId != null) {
            authentication.destroyAuthentication(tokenId);
        }
        if (signUpData == null ||
                !Validator.notEmpty(signUpData.getEmail()).email().isValid() ||
                !Validator.notEmpty(signUpData.getNickname()).isValid() ||
                !Validator.notEmpty(signUpData.getPassword()).isValid()) {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setCode(HttpStatus.BAD_REQUEST.value());
            json.setMessage("Bad data format, or missing required fields, please try reload page and try again.");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        try {
            authentication.createUser(signUpData.getNickname(), signUpData.getEmail(), signUpData.getPassword(), signUpData.getSurname(), signUpData.getName());
        } catch (NicknameAlreadyExistsException | EmailAlreadyExistsException exception) {
            return conflictResponse(exception.getMessage());
        }
        String newTokenId = authentication.createAuthentication(signUpData.getEmail(), signUpData.getPassword());
        if (newTokenId != null) {
            LoggedUserDto json = getLoggedUserJson(newTokenId);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setMessage("Something went wrong during sign up");
            json.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private LoggedUserDto getLoggedUserJson(String tokenId) {
        User user = authentication.getAuthenticatedUser(tokenId).getUser();
        LoggedUserDto json = new LoggedUserDto();
        json.setId(user.getId());
        json.setLastSignInDate(user.getLastSignInDate());
        json.setRole(user.getRole().getId());
        json.setNickname(user.getNickname());
        json.setTokenId(tokenId);
        return json;
    }
}
