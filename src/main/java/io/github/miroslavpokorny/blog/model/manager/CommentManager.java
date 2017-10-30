package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Comment;

import java.util.List;

public interface CommentManager {
    Comment createComment(Comment comment);
    Comment createComment(int articleId, int userId, String commentText, boolean visible);
    Comment createComment(int articleId, int userId, String commentText, boolean visible, Integer parentComment);
    List<Comment> getAllCommentsForArticleByArticleId(int id);
    Comment getCommentById(int id);
    void hideCommentById(int id);
}
