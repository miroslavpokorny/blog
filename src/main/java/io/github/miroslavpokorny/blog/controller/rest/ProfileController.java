package io.github.miroslavpokorny.blog.controller.rest;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.dto.ChangePasswordDto;
import io.github.miroslavpokorny.blog.model.dto.ProfileInfoDto;
import io.github.miroslavpokorny.blog.model.error.NicknameAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.dto.RequestByIdDto;
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
    public ResponseEntity loadProfile(@RequestBody RequestByIdDto request, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (!isAccessAllowed(tokenId, request.getId())) {
            return forbiddenResponse();
        }
        User user = userManager.getUserById(request.getId());
        ProfileInfoDto json = new ProfileInfoDto();
        json.setId(user.getId());
        json.setName(user.getName());
        json.setSurname(user.getSurname());
        json.setNickname(user.getNickname());
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping("/api/profile/edit")
    public ResponseEntity editProfile(@RequestBody ProfileInfoDto profile, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!isAccessAllowed(tokenId, profile.getId())) {
            return forbiddenResponse();
        }
        User user = userManager.getUserById(profile.getId());
        user.setName(profile.getName());
        user.setNickname(profile.getNickname());
        user.setSurname(profile.getSurname());
        try {
            userManager.updateUser(user);
        } catch (NicknameAlreadyExistsException exception) {
            return conflictResponse(exception.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/api/profile/changePassword")
    public ResponseEntity changePassword(@RequestBody ChangePasswordDto changePasswordDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (!isAccessAllowed(tokenId, changePasswordDto.getUserId())) {
            return forbiddenResponse();
        }
        User user = userManager.getUserById(changePasswordDto.getUserId());
        if (user == null) {
            return notFoundResponse("User was not found!");
        }
        if (!authentication.checkPassword(changePasswordDto.getOldPassword(), user.getPassword())) {
            return forbiddenResponse("Password is incorrect!");
        }
        user.setPassword(authentication.hashPassword(changePasswordDto.getNewPassword()));
        userManager.updateUser(user);
        authentication.getAuthenticatedUser(tokenId).reloadUserInfo();
        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean isAccessAllowed(String tokenId, int requestId) {
        return (this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.ADMINISTRATOR) ||
                this.authentication.getAuthenticatedUser(tokenId).getUser().getId() == requestId);
    }
}
