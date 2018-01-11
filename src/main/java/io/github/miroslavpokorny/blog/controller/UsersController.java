package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.UserRole;
import io.github.miroslavpokorny.blog.model.dto.*;
import io.github.miroslavpokorny.blog.model.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class UsersController extends AuthorizeController {
    private final Authentication authentication;

    private final UserManager userManager;

    @Autowired
    public UsersController(Authentication authentication, UserManager userManager) {
        this.authentication = authentication;
        this.userManager = userManager;
    }

    @RequestMapping("/api/users/list")
    public ResponseEntity getUsersList(@RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (!isAccessAllowed(tokenId)) {
            return forbiddenResponse();
        }
        List<User> users = userManager.getAllUsers();
        List<UserInfoDto> usersInfo = users.stream().map(user -> {
            UserInfoDto info = new UserInfoDto();
            info.setEmail(user.getEmail());
            info.setId(user.getId());
            info.setName(user.getName());
            info.setNickname(user.getNickname());
            info.setSurname(user.getSurname());
            info.setRole(user.getRole().getId());
            info.setEnabled(user.isEnabled());
            return info;
        }).collect(Collectors.toList());
        UsersListDto json = new UsersListDto();
        json.setUsers(usersInfo);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    private boolean isAccessAllowed(String tokenId) {
        return this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.ADMINISTRATOR);
    }

    @RequestMapping("/api/users/switchEnabledState")
    public ResponseEntity switchEnabledState(@RequestBody RequestByIdDto request, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (!this.isAccessAllowed(tokenId)) {
            return forbiddenResponse();
        }
        if (authentication.getAuthenticatedUser(tokenId).getUser().getId() == request.getId()) {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setMessage("You can't disable your account!");
            json.setCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(json, HttpStatus.CONFLICT);
        }
        User user = userManager.getUserById(request.getId());
        if (user == null) {
            return notFoundResponse("User not found!");
        }
        user.setEnabled(!user.isEnabled());
        userManager.updateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/users/changeRole")
    public ResponseEntity switchEnabledState(@RequestBody ChangeUserRoleDto request, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (!this.isAccessAllowed(tokenId)) {
            return forbiddenResponse();
        }
        if (authentication.getAuthenticatedUser(tokenId).getUser().getId() == request.getUserId()) {
            ErrorMessageDto json = new ErrorMessageDto();
            json.setMessage("You can't change role of your account!");
            json.setCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(json, HttpStatus.CONFLICT);
        }
        User user = userManager.getUserById(request.getUserId());
        if (user == null) {
            return notFoundResponse("User not found!");
        }
        UserRole userRole = userManager.getUserRoleById(request.getRole());
        if (userRole == null) {
            return notFoundResponse("User role not found!");
        }
        user.setRole(userRole);
        userManager.updateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }
}
