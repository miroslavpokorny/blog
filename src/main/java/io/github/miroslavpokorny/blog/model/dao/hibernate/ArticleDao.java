package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ArticleDao extends DaoBase<Article> implements io.github.miroslavpokorny.blog.model.dao.ArticleDao {
    @Override
    public List<Article> getAllByUserId(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Boolean> visibleExpression = criteriaBuilder.parameter(Boolean.class);
            CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
            Root<Article> from = criteriaQuery.from(Article.class);
            from.fetch("author");
            from.fetch("category");
            from.fetch("gallery", JoinType.LEFT);
            criteriaQuery.where(criteriaBuilder.equal(from.get("visible"), visibleExpression));
            ParameterExpression<Integer> authorId = criteriaBuilder.parameter(Integer.class);
            criteriaQuery.where(criteriaBuilder.equal(from.get("author"), authorId));
            Query<Article> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(authorId, id);
            query.setParameter(visibleExpression, true);
            return query.getResultList();
        }
    }

    @Override
    public PaginationHelper<Article> getNewestArticles(int page, int itemsPerPage) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Boolean> visibleExpression = criteriaBuilder.parameter(Boolean.class);

            CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
            Root<Article> from = criteriaQuery.from(Article.class);
            criteriaQuery.orderBy(criteriaBuilder.desc(from.get("publishDate")));
            criteriaQuery.where(criteriaBuilder.equal(from.get("visible"), visibleExpression));
            from.fetch("author");
            from.fetch("category");
            from.fetch("gallery", JoinType.LEFT);
            Query<Article> query = session.delegate().createQuery(criteriaQuery);
            query.setFirstResult((page - 1) * itemsPerPage);
            query.setMaxResults(itemsPerPage);
            query.setParameter(visibleExpression, true);

            CriteriaQuery<Long> totalCriteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Article> fromTotal = totalCriteriaQuery.from(Article.class);
            totalCriteriaQuery.select(criteriaBuilder.count(fromTotal));
            totalCriteriaQuery.where(criteriaBuilder.equal(fromTotal.get("visible"), visibleExpression));
            Query<Long> totalQuery = session.delegate().createQuery(totalCriteriaQuery);
            totalQuery.setParameter(visibleExpression, true);

            return createPaginationHelper(page, itemsPerPage, query, totalQuery);
        }
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesInCategory(int page, int itemsPerPage, int categoryId) {
        return getNewestArticlesByColumnValue(page, itemsPerPage, "category", categoryId);
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesByUserId(int page, int itemsPerPage, int userId) {
        return getNewestArticlesByColumnValue(page, itemsPerPage, "author", userId);
    }

    private PaginationHelper<Article> getNewestArticlesByColumnValue(int page, int itemsPerPage, String columnName, int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Integer> authorIdExpression = criteriaBuilder.parameter(Integer.class);
            ParameterExpression<Boolean> visibleExpression = criteriaBuilder.parameter(Boolean.class);

            CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
            Root<Article> from = criteriaQuery.from(Article.class);
            criteriaQuery.orderBy(criteriaBuilder.desc(from.get("publishDate")));
            from.fetch("author");
            from.fetch("category");
            from.fetch("gallery", JoinType.LEFT);
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(from.get("visible"), visibleExpression),
                            criteriaBuilder.equal(from.get(columnName), authorIdExpression)
                    )
            );
            Query<Article> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(authorIdExpression, id);
            query.setFirstResult((page - 1) * itemsPerPage);
            query.setMaxResults(itemsPerPage);

            CriteriaQuery<Long> totalCriteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Article> fromTotal = totalCriteriaQuery.from(Article.class);
            totalCriteriaQuery.select(criteriaBuilder.count(fromTotal));
            totalCriteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(fromTotal.get("visible"), visibleExpression),
                            criteriaBuilder.equal(fromTotal.get(columnName), authorIdExpression)
                    )
            );
            Query<Long> totalQuery = session.delegate().createQuery(totalCriteriaQuery);
            totalQuery.setParameter(authorIdExpression, id);

            return createPaginationHelper(page, itemsPerPage, query, totalQuery);
        }
    }

    @Override
    public PaginationHelper<Article> getArticlesSearch(int page, int itemsPerPage, String searchQuery) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<String> searchExpression = criteriaBuilder.parameter(String.class);
            ParameterExpression<Boolean> visibleExpression = criteriaBuilder.parameter(Boolean.class);

            CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
            Root<Article> from = criteriaQuery.from(Article.class);
            criteriaQuery.orderBy(criteriaBuilder.desc(from.get("publishDate")));
            from.fetch("author");
            from.fetch("category");
            from.fetch("gallery", JoinType.LEFT);
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(from.get("visible"), visibleExpression),
                            criteriaBuilder.or(
                                    criteriaBuilder.like(from.get("name"), searchExpression),
                                    criteriaBuilder.like(from.get("content"), searchExpression)
                            )
                    )
            );
            Query<Article> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(visibleExpression, true);
            query.setParameter(searchExpression, String.format("%%%s%%", searchQuery));
            query.setFirstResult((page - 1) * itemsPerPage);
            query.setMaxResults(itemsPerPage);

            CriteriaQuery<Long> totalCriteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Article> fromTotal = totalCriteriaQuery.from(Article.class);
            totalCriteriaQuery.select(criteriaBuilder.count(fromTotal));
            totalCriteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(fromTotal.get("visible"), visibleExpression),
                            criteriaBuilder.or(
                                    criteriaBuilder.like(fromTotal.get("name"), searchExpression),
                                    criteriaBuilder.like(fromTotal.get("content"), searchExpression)
                            )
                    )
            );
            Query<Long> totalQuery = session.delegate().createQuery(totalCriteriaQuery);
            totalQuery.setParameter(visibleExpression, true);
            totalQuery.setParameter(searchExpression, String.format("%%%s%%", searchQuery));

            return createPaginationHelper(page, itemsPerPage, query, totalQuery);
        }
    }
}
