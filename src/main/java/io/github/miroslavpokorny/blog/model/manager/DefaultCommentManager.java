package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Comment;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class DefaultCommentManager implements CommentManager {
    @Override
    public Comment createComment(Comment comment) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Comment createComment(int articleId, int userId, String commentText) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Comment createComment(int articleId, int userId, String commentText, Integer parentComment) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<Comment> getAllCommentsForArticleByArticleId(int id) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Comment getCommentById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Comment hideCommentById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
