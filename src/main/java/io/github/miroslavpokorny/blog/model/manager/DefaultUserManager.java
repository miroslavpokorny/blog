package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.UserQuestion;
import io.github.miroslavpokorny.blog.model.UserRole;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import io.github.miroslavpokorny.blog.model.dao.UserQuestionDao;
import io.github.miroslavpokorny.blog.model.dao.UserRoleDao;
import io.github.miroslavpokorny.blog.model.error.EmailAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.error.NicknameAlreadyExistsException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultUserManager implements UserManager{
    private static final String DEFAULT_AVATAR_NAME = "avatar.png";

    private final UserDao userDao;

    private final UserRoleDao userRoleDao;

    private final UserQuestionDao userQuestionDao;

    @Autowired
    public DefaultUserManager(UserDao userDao, UserRoleDao userRoleDao, UserQuestionDao userQuestionDao) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.userQuestionDao = userQuestionDao;
    }

    @Override
    public User createUser(String email, String password, String nickname) {
        return createUser(email, password, nickname, null, null, true, DEFAULT_AVATAR_NAME);
    }

    @Override
    public User createUser(String email, String password, String nickname, String name) {
        return createUser(email, password, nickname, name, null, true, DEFAULT_AVATAR_NAME);
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname) {
        return createUser(email, password, nickname, name, surname, true, DEFAULT_AVATAR_NAME);
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname, boolean enabled) {
        return createUser(email, password, nickname, name, surname, enabled, DEFAULT_AVATAR_NAME);
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname, boolean enabled, String avatar) {
        User user = new User();
        user.setEmail(email);
        user.setRole(userRoleDao.loadById(1));
        user.setActivated(false);
        user.setActivationEmailKey(UUID.randomUUID().toString());
        user.setLastSignInDate(new Date());
        user.setAvatar(avatar);
        user.setEnabled(enabled);
        user.setSurname(surname);
        user.setName(name);;
        user.setPassword(password);
        user.setNickname(nickname);
        user.setActivated(true);
        return createUser(user);
    }

    @Override
    public User createUser(User user) {
        try {
            return userDao.create(user);
        } catch (ConstraintViolationException exception) {
            if (exception.getConstraintName().equals("Nickname_UNIQUE")) {
                throw new NicknameAlreadyExistsException("User with nickname '" + user.getNickname() + "' already exists!", exception);
            }
            if (exception.getConstraintName().equals("Email_UNIQUE")) {
                throw new EmailAlreadyExistsException("User with email '" + user.getEmail() + "' already exists!", exception);
            }
            throw exception;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void updateUser(int id, String name, String surname, String nickname) {
        User user = userDao.getById(id);
        user.setName(name);
        user.setSurname(surname);
        user.setNickname(nickname);
        updateUser(user);
    }

    @Override
    public void updateUser(int id, String name, String surname, String nickname, boolean enabled) {
        User user = userDao.getById(id);
        user.setName(name);
        user.setSurname(surname);
        user.setNickname(nickname);
        user.setEnabled(enabled);
        updateUser(user);
    }

    @Override
    public void updateUser(int id, String name, String surname, String nickname, boolean enabled, int role) {
        User user = userDao.getById(id);
        user.setName(name);
        user.setSurname(surname);
        user.setNickname(nickname);
        user.setEnabled(enabled);
        user.setRole(userRoleDao.loadById(role));
        updateUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Override
    public void changeUserAvatarById(int id, String avatarFileName) {
        User user = userDao.getById(id);
        user.setAvatar(avatarFileName);
        updateUser(user);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getById(id);
    }

    @Override
    public UserQuestion createQuestion(int userId, String answer, String question) {
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setAnswer(answer);
        userQuestion.setQuestion(question);
        userQuestion.setUser(userDao.loadById(userId));
        return createQuestion(userQuestion);
    }

    @Override
    public UserQuestion createQuestion(UserQuestion userQuestion) {
        return userQuestionDao.create(userQuestion);
    }

    @Override
    public UserQuestion getUserQuestionById(int id) {
        return userQuestionDao.getById(id);
    }

    @Override
    public void updateUserQuestion(int id, String answer, String question) {
        UserQuestion userQuestion = userQuestionDao.loadById(id);
        userQuestion.setAnswer(answer);
        userQuestion.setQuestion(question);
        updateUserQuestion(userQuestion);
    }

    @Override
    public void updateUserQuestion(UserQuestion userQuestion) {
        userQuestionDao.update(userQuestion);
    }

    @Override
    public void deleteQuestion(int id) {
        userQuestionDao.delete(userQuestionDao.loadById(id));
    }

    @Override
    public void updateUserPassword(int id, String newPassword) {
        User user = userDao.getById(id);
        user.setPassword(newPassword);
        updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleDao.getAll();
    }

    @Override
    public String setNewRestorePasswordKeyToUserById(int userId) {
        User user = userDao.getById(userId);
        String key = UUID.randomUUID().toString();
        user.setRestorePasswordKey(key);
        userDao.update(user);
        return key;
    }
}
