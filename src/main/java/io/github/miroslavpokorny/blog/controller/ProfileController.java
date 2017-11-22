package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.error.NicknameAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.json.ErrorMessageJson;
import io.github.miroslavpokorny.blog.model.json.ProfileInfoJson;
import io.github.miroslavpokorny.blog.model.json.RequestByIdJson;
import io.github.miroslavpokorny.blog.model.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ProfileController extends AuthorizeController {

    private final Authentication authentication;

    private final UserManager userManager;

    @Autowired
    public ProfileController(Authentication authentication, UserManager userManager) {
        this.authentication = authentication;
        this.userManager = userManager;
    }

    @RequestMapping("/api/profile/load")
    public ResponseEntity loadProfile(@RequestBody RequestByIdJson request, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!isAccessAllowed(tokenId, request.getId())) {
            return unAuthorizedResponse();
        }
        User user = userManager.getUserById(request.getId());
        ProfileInfoJson json = new ProfileInfoJson();
        json.setId(user.getId());
        json.setName(user.getName());
        json.setSurname(user.getSurname());
        json.setNickname(user.getNickname());
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping("/api/profile/edit")
    public ResponseEntity editProfile(@RequestBody ProfileInfoJson profile, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!isAccessAllowed(tokenId, profile.getId())) {
            return unAuthorizedResponse();
        }
        User user = userManager.getUserById(profile.getId());
        user.setName(profile.getName());
        user.setNickname(profile.getNickname());
        user.setSurname(profile.getSurname());
        try {
            userManager.updateUser(user);
        } catch (NicknameAlreadyExistsException exception) {
            ErrorMessageJson json = new ErrorMessageJson();
            json.setCode(HttpStatus.CONFLICT.value());
            json.setMessage(exception.getMessage());
            return new ResponseEntity<>(json, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isAccessAllowed(String tokenId, int requestId) {
        return (this.authentication.isAuthenticate(tokenId) &&
                (this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.ADMINISTRATOR) ||
                this.authentication.getAuthenticatedUser(tokenId).getUser().getId() == requestId));
    }
}