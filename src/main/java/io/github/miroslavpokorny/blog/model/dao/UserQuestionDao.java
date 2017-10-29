package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserQuestion;

import java.util.List;

public interface UserQuestionDao extends Dao<UserQuestion> {
    List<UserQuestion> getAllQuestionsByUserId(int id);
}
