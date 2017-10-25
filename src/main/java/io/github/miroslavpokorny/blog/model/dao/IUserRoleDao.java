package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserRole;

public interface IUserRoleDao extends IDao<UserRole> {
    UserRole getByName(String name);
}
