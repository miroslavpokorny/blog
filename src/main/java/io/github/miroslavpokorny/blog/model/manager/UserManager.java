package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.UserQuestion;
import io.github.miroslavpokorny.blog.model.UserRole;

import java.util.List;

public interface UserManager {
    User createUser(String email, String password, String nickname);
    User createUser(String email, String password, String nickname, String name);
    User createUser(String email, String password, String nickname, String name, String surname);
    User createUser(String email, String password, String nickname, String name, String surname, boolean enabled);
    User createUser(String email, String password, String nickname, String name, String surname, boolean enabled, String avatar);
    User createUser(User user);
    User getUserByEmail(String email);
    void updateUser(int id, String name, String surname, String nickname);
    void updateUser(int id, String name, String surname, String nickname, boolean enabled);
    void updateUser(int id, String name, String surname, String nickname, boolean enabled, int role);
    void updateUser(User user);
    void changeUserAvatarById(int id, String avatarFileName);
    User getUserById(int id);
    UserQuestion createQuestion(int userId, String answer, String question);
    UserQuestion createQuestion(UserQuestion userQuestion);
    UserQuestion getUserQuestionById(int id);
    void updateUserQuestion(int id, String answer, String question);
    void updateUserQuestion(UserQuestion userQuestion);
    void deleteQuestion(int id);
    void updateUserPassword(int id, String newPassword);
    List<User> getAllUsers();
    List<UserRole> getAllUserRoles();
    String setNewRestorePasswordKeyToUserById(int userId);
    UserRole getUserRoleById(int id);
}
