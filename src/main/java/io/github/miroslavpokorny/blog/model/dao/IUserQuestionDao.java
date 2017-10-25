package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserQuestion;

import java.util.List;

public interface IUserQuestionDao extends IDao<UserQuestion> {
    List<UserQuestion> getAllQuestionsByUserId(int id);
}
