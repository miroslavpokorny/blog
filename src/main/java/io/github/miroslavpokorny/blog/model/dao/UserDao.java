package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.User;

public interface UserDao extends Dao<User> {
    User getByNickname(String nickname);

    User getByEmail(String email);
}
