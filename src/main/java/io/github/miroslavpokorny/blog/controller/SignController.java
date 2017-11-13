package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.helper.GsonHelper;
import io.github.miroslavpokorny.blog.model.json.ErrorMessageJson;
import io.github.miroslavpokorny.blog.model.json.LoggedUserJson;
import io.github.miroslavpokorny.blog.model.json.SignInCredentialsJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class SignController{
    private final Authentication authentication;

    @Autowired
    public SignController(Authentication authentication) {
        this.authentication = authentication;
    }

    @RequestMapping(value = "/api/sign/in", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestBody SignInCredentialsJson credentials, @RequestParam(value = "tokenId", required = false) String tokenId) {
        if (tokenId != null) {
            authentication.destroyAuthentication(tokenId);
        }
        if (credentials == null || credentials.getEmail() == null || credentials.getPassword() == null) {
            ErrorMessageJson json = new ErrorMessageJson();
            json.setCode(400);
            json.setMessage("Bad data format, please try reload page and try again.");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        String newTokenId = authentication.createAuthentication(credentials.getEmail(), credentials.getPassword());
        if (newTokenId != null) {
            LoggedUserJson json = getLoggedUserJson(newTokenId);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            ErrorMessageJson json = new ErrorMessageJson();
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
            LoggedUserJson json = getLoggedUserJson(tokenId);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping("/api/sign/ping")
    public String isSigned(@RequestParam(value = "tokenId", required = false) String tokenId) {
        return authentication.isAuthenticate(tokenId) ? "Auth" : "Not auth";
    }

    private LoggedUserJson getLoggedUserJson(String tokenId) {
        User user = authentication.getAuthenticatedUser(tokenId).getUser();
        LoggedUserJson json = new LoggedUserJson();
        json.setId(user.getId());
        json.setLastSignInDate(user.getLastSignInDate());
        json.setRole(user.getRole().getId());
        json.setNickname(user.getNickname());
        json.setTokenId(tokenId);
        return json;
    }
}
