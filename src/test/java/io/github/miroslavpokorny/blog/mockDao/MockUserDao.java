package io.github.miroslavpokorny.blog.mockDao;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import io.github.miroslavpokorny.blog.model.dao.UserRoleDao;
import io.github.miroslavpokorny.blog.model.error.EmailAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.error.NicknameAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

@Primary
@Repository
@Profile("TEST")
public class MockUserDao implements UserDao {
    private List<User> users = new ArrayList<>();

    private UserRoleDao userRoleDao = new MockUserRoleDao();
    public MockUserDao() {
        User user = new User(
                "Franta",
                "Novak",
                "frantanovak@example.com",
                "$2a$10$a92.N8GAQDerLWsIAtTMbuY6EpxmIEB9jQ1qHBJiWY6ohm1q1usCe", // abc
                "frantanovak",
                new GregorianCalendar(2017, GregorianCalendar.JANUARY, 1).getTime(),
                userRoleDao.getByName("Administrator"),
                true,
                "",
                "",
                "",
                true);
        user.setId(1);
        users.add(user);
        user = new User(
                "Pepa",
                "Mizera",
                "pepamizera@example.com",
                "$2a$10$ZLgyzYvtd3rlfuw4KYqbPOFZc75RaXmC9j2v9Itxp5.MLm6.HqHYW", // 123
                "pepamizera",
                new GregorianCalendar(2018, GregorianCalendar.JANUARY, 1).getTime(),
                userRoleDao.getByName("User"),
                true,
                "",
                "",
                "",
                true);
        user.setId(2);
        users.add(user);
    }

    @Override
    public User getByNickname(String nickname) {
        return users.stream().filter(user -> user.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public PaginationHelper<User> getAllWithPagination(int page, int itemsPerPage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User create(User entity) {
        User sameNickname = users.stream().filter(user -> user.getNickname().equals(entity.getNickname())).findFirst().orElse(null);
        if (sameNickname != null) {
            throw new ConstraintViolationException("User with this nickname already exist", null, "Nickname_UNIQUE");
        }
        User sameEmail = users.stream().filter(user -> user.getEmail().equals(entity.getEmail())).findFirst().orElse(null);
        if (sameEmail != null) {
            throw new ConstraintViolationException("User with this email already exist", null, "Email_UNIQUE");
        }
        int maxId = users.stream().max(Comparator.comparingInt(User::getId)).orElseGet(() -> {
            User user = new User();
            user.setId(1);
            return user;
        }).getId();
        entity.setId(maxId + 1);
        users.add(entity);
        return entity;
    }

    @Override
    public void update(User entity) {
        User toEdit = users.stream().filter(user -> user.getId() == entity.getId()).findFirst().orElse(null);
        if (toEdit == null) {
            return;
        }
        users.remove(toEdit);
        users.add(entity);
    }

    @Override
    public void delete(User entity) {
        User toDelete = users.stream().filter(users -> users.getId() == entity.getId()).findFirst().orElse(null);
        if (toDelete == null) {
            return;
        }
        users.remove(toDelete);
    }

    @Override
    public void saveOrUpdate(User entity) {
        users.stream().filter(user -> user.getId() == entity.getId()).findFirst().ifPresent(toEdit -> users.remove(toEdit));
        users.add(entity);
    }

    @Override
    public User getById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User loadById(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
