package io.github.miroslavpokorny.blog.authentication;

import io.github.miroslavpokorny.blog.model.User;

public interface AuthenticatedUser {
    User getUser();
    boolean isUserInRole(int role);
    void reloadUserInfo();
}
