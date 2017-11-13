package io.github.miroslavpokorny.blog.authentication;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.manager.UserManager;

public class AuthenticatedUserImpl implements AuthenticatedUser {
    private final User user;

    public AuthenticatedUserImpl(UserManager userManager, User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isUserInRole(int role) {
        return this.user.getRole().getId() >= role;
    }
}
