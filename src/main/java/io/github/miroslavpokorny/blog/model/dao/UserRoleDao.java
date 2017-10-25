package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserRole;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UserRoleDao extends  DaoBase<UserRole> implements IUserRoleDao{
    @Override
    public UserRole getByName(String name) {
        // TODO implement
        throw new NotImplementedException();
    }
}
