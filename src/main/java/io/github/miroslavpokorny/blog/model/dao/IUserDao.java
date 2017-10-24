package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.User;

public interface IUserDao extends IDao<User>{
    User getByNickname(String nickname);

    User getByEmail(String email);
}
