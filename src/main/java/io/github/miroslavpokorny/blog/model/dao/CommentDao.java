package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Comment;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CommentDao extends DaoBase<Comment> implements ICommentDao {
    private final IArticleDao articleDao;

    @Autowired
    public CommentDao(IArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Override
    public List<Comment> getAllCommentsForArticle(int articleId) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Article> articleExpression = criteriaBuilder.parameter(Article.class);
            CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
            Root<Comment> from = criteriaQuery.from(Comment.class);
            from.fetch("author");
            from.fetch("subComments");
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(from.get("article"), articleExpression),
                            criteriaBuilder.isNull(from.get("parentComment"))
                    )
            );

            Query<Comment> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(articleExpression, articleDao.loadById(articleId));
            return query.getResultList();
        }
    }

    @Override
    public Comment getCommentById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Integer> commentIdExpression = criteriaBuilder.parameter(Integer.class);
            CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
            Root<Comment> from = criteriaQuery.from(Comment.class);
            from.fetch("author");
            criteriaQuery.where(criteriaBuilder.equal(from.get("id"), commentIdExpression));
            Query<Comment> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(commentIdExpression, id);
            return query.getSingleResult();
        }
    }
}
