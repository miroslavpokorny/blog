package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.ArticleRating;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.dao.ArticleDao;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

@Repository
public class ArticleRatingDao extends DaoBase<ArticleRating> implements io.github.miroslavpokorny.blog.model.dao.ArticleRatingDao {

    private final UserDao userDao;

    private final ArticleDao articleDao;

    @Autowired
    public ArticleRatingDao(UserDao userDao, ArticleDao articleDao) {
        this.userDao = userDao;
        this.articleDao = articleDao;
    }
    //    public List<Object[]> getAvgRatingForArticles(List<Integer> articleIds) {
//        try (CloseableSession session = HibernateHelper.getSession()) {
//
//                    //TODO Write this query
//        }
//    }

    @Override
    public float getAvgRatingForArticleById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Article> articleExpression = criteriaBuilder.parameter(Article.class);
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<ArticleRating> from = criteriaQuery.from(ArticleRating.class);
            criteriaQuery.where(criteriaBuilder.equal(from.get("article"), articleExpression));
            criteriaQuery.select(criteriaBuilder.avg(from.get("rating")));
            Query<Double> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(articleExpression, articleDao.loadById(id));
            return query.getSingleResult().floatValue();
        }
    }

    @Override
    public ArticleRating getArticleRatingByArticleIdAndUserId(int articleId, int userId) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Article> articleExpression = criteriaBuilder.parameter(Article.class);
            ParameterExpression<User> userExpression = criteriaBuilder.parameter(User.class);
            CriteriaQuery<ArticleRating> criteriaQuery = criteriaBuilder.createQuery(ArticleRating.class);
            Root<ArticleRating> from = criteriaQuery.from(ArticleRating.class);
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(from.get("article"), articleExpression),
                            criteriaBuilder.equal(from.get("user"), userExpression)
                    )
            );
            Query<ArticleRating> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(articleExpression, articleDao.loadById(articleId));
            query.setParameter(userExpression, userDao.loadById(userId));
            return query.getSingleResult();
        }
    }
}