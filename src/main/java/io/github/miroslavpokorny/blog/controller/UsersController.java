package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.json.UserInfoDto;
import io.github.miroslavpokorny.blog.model.json.UsersListDto;
import io.github.miroslavpokorny.blog.model.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO switch user state endpoint
}
