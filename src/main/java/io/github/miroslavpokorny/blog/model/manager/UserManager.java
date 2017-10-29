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
    User getUserByEmail(String email);
    User updateUser(int id, String name, String surname, String nickname);
    User updateUser(int id, String name, String surname, String nickname, boolean enabled);
    User updateUser(int id, String name, String surname, String nickname, boolean enabled, int role);
    User updateUser(User user);
    boolean changeUserPasswordById(int id, String oldPassword, String newPassword);
    boolean changeUserAvatarById(int id, String avatarFileName);
    User getUserById(int id);
    UserQuestion createQuestion(int userId, String answer, String question);
    UserQuestion createQuestion(UserQuestion userQuestion);
    UserQuestion getUserQuestionById(int id);
    UserQuestion updateUserQuestion(int id, String answer, String question);
    void deleteQuestion(int id);
    void updateUserPassword(int id, String newPassword);
    List<User> getAllUsers();
    List<UserRole> getAllUserRoles();
}
