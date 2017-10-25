package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Comment;

import java.util.List;

public interface ICommentDao extends IDao<Comment> {
    List<Comment> getAllCommentsForArticle(int articleId);
    Comment getCommentById(int id);
}
