package io.github.miroslavpokorny.blog.authentication;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.manager.UserManager;

public class AuthenticatedUserImpl implements AuthenticatedUser {
    private User user;
    private final UserManager userManager;

    public AuthenticatedUserImpl(UserManager userManager, User user) {
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isUserInRole(int role) {
        return this.user.getRole().getId() >= role;
    }

    @Override
    public void reloadUserInfo() {
        User reloadedUser = userManager.getUserById(user.getId());
        if (reloadedUser != null) {
            user = reloadedUser;
        }
    }
}
