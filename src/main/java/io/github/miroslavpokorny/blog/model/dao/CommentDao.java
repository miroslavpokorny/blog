package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Comment;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Repository
public class CommentDao extends DaoBase<Comment> implements ICommentDao {
    @Override
    public List<Comment> getAllCommentsForArticle(int articleId) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Comment getCommentById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
