package io.github.miroslavpokorny.blog.mockDao;

import io.github.miroslavpokorny.blog.model.UserRole;
import io.github.miroslavpokorny.blog.model.dao.UserRoleDao;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Primary
@Repository
@Profile("TEST")
public class MockUserRoleDao implements UserRoleDao {
    private List<UserRole> userRoles = new ArrayList<>();

    public MockUserRoleDao() {
        userRoles.add(new UserRole(1, "User", "Basic user"));
        userRoles.add(new UserRole(2, "Editor", "Editor"));
        userRoles.add(new UserRole(3, "Moderator", "Moderator"));
        userRoles.add(new UserRole(4, "Administrator", "Administrator"));
    }

    @Override
    public UserRole getByName(String name) {
        return userRoles.stream().filter(userRole -> userRole.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<UserRole> getAll() {
        return userRoles;
    }

    @Override
    public PaginationHelper<UserRole> getAllWithPagination(int page, int itemsPerPage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserRole create(UserRole entity) {
        int maxId = userRoles.stream().max(Comparator.comparingInt(UserRole::getId)).orElse(new UserRole(1, null, null)).getId();
        entity.setId(maxId + 1);
        userRoles.add(entity);
        return entity;
    }

    @Override
    public void update(UserRole entity) {
        UserRole toEdit = userRoles.stream().filter(userRole -> userRole.getId() == entity.getId()).findFirst().orElse(null);
        if (toEdit == null) {
            return;
        }
        userRoles.remove(toEdit);
        userRoles.add(entity);
    }

    @Override
    public void delete(UserRole entity) {
        UserRole toDelete = userRoles.stream().filter(userRole -> userRole.getId() == entity.getId()).findFirst().orElse(null);
        if (toDelete == null) {
            return;
        }
        userRoles.remove(toDelete);
    }

    @Override
    public void saveOrUpdate(UserRole entity) {
        userRoles.stream().filter(userRole -> userRole.getId() == entity.getId()).findFirst().ifPresent(toEdit -> userRoles.remove(toEdit));
        userRoles.add(entity);
    }

    @Override
    public UserRole getById(int id) {
        return userRoles.stream().filter(userRole -> userRole.getId() == id).findFirst().orElse(null);
    }

    @Override
    public UserRole loadById(int id) {
        return new UserRole(id, null, null);
    }
}
