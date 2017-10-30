package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Comment;
import io.github.miroslavpokorny.blog.model.dao.ArticleDao;
import io.github.miroslavpokorny.blog.model.dao.CommentDao;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DefaultCommentManager implements CommentManager {
    private final CommentDao commentDao;

    private final ArticleDao articleDao;

    private final UserDao userDao;

    @Autowired
    public DefaultCommentManager(CommentDao commentDao, ArticleDao articleDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.articleDao = articleDao;
        this.userDao = userDao;
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentDao.create(comment);
    }

    @Override
    public Comment createComment(int articleId, int userId, String commentText, boolean visible) {
        return createComment(articleId, userId, commentText, visible, null);
    }

    @Override
    public Comment createComment(int articleId, int userId, String commentText, boolean visible, Integer parentComment) {
        Comment comment = new Comment();
        comment.setArticle(articleDao.loadById(articleId));
        comment.setAuthor(userDao.loadById(userId));
        comment.setContent(commentText);
        comment.setParentComment(parentComment != null ? commentDao.loadById(parentComment) : null);
        comment.setPublishDate(new Date());
        comment.setVisible(visible);
        return createComment(comment);
    }

    @Override
    public List<Comment> getAllCommentsForArticleByArticleId(int id) {
        return commentDao.getAllCommentsForArticle(id);
    }

    @Override
    public Comment getCommentById(int id) {
        return commentDao.getCommentById(id);
    }

    @Override
    public void hideCommentById(int id) {
        Comment comment = commentDao.getById(id);
        comment.setVisible(false);
        commentDao.update(comment);
    }
}
