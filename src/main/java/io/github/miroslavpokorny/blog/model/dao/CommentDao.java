package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Comment;

import java.util.List;

public interface CommentDao extends Dao<Comment> {
    List<Comment> getAllCommentsForArticle(int articleId);
    Comment getCommentById(int id);
}
