package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.UserQuestion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class UserQuestionDao extends DaoBase<UserQuestion> implements IUserQuestionDao {
    @Override
    public List<UserQuestion> getAllQuestionsByUserId(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
