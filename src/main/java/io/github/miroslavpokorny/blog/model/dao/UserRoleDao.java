package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserRole;

public interface UserRoleDao extends Dao<UserRole> {
    UserRole getByName(String name);
}
