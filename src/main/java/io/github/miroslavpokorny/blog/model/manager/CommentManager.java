package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Comment;

import java.util.List;

public interface CommentManager {
    Comment createComment(Comment comment);
    Comment createComment(int articleId, int userId, String commentText);
    Comment createComment(int articleId, int userId, String commentText, Integer parentComment);
    List<Comment> getAllCommentsForArticleByArticleId(int id);
    Comment getCommentById(int id);
    Comment hideCommentById(int id);
}
