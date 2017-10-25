package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.ArticleRating;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.List;

public class ArticleRatingDao extends DaoBase<ArticleRating> implements IArticleRatingDao {

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
            ArticleDao ad = new ArticleDao();
            Query<Double> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(articleExpression, ad.loadById(id));
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
            ArticleDao ad = new ArticleDao();
            UserDao ud = new UserDao();
            Query<ArticleRating> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(articleExpression, ad.loadById(articleId));
            query.setParameter(userExpression, ud.loadById(userId));
            return query.getSingleResult();
        }
    }
}
