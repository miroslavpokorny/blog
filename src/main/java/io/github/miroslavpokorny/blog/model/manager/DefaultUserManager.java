package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.UserQuestion;
import io.github.miroslavpokorny.blog.model.UserRole;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class DefaultUserManager implements UserManager{
    @Override
    public User createUser(String email, String password, String nickname) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User createUser(String email, String password, String nickname, String name) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname, boolean enabled) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User createUser(String email, String password, String nickname, String name, String surname, boolean enabled, String avatar) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User getUserByEmail(String email) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User updateUser(int id, String name, String surname, String nickname) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User updateUser(int id, String name, String surname, String nickname, boolean enabled) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User updateUser(int id, String name, String surname, String nickname, boolean enabled, int role) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User updateUser(User user) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public boolean changeUserPasswordById(int id, String oldPassword, String newPassword) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public boolean changeUserAvatarById(int id, String avatarFileName) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public User getUserById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public UserQuestion createQuestion(int userId, String answer, String question) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public UserQuestion createQuestion(UserQuestion userQuestion) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public UserQuestion getUserQuestionById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public UserQuestion updateUserQuestion(int id, String answer, String question) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public void deleteQuestion(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public void updateUserPassword(int id, String newPassword) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<User> getAllUsers() {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        //TODO implement
        throw new NotImplementedException();
    }
}
