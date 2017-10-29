package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.UserRole;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

@Repository
public class UserRoleDao extends DaoBase<UserRole> implements io.github.miroslavpokorny.blog.model.dao.UserRoleDao {
    @Override
    public UserRole getByName(String name) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<String> nameExpression = criteriaBuilder.parameter(String.class);
            CriteriaQuery<UserRole> criteriaQuery = criteriaBuilder.createQuery(UserRole.class);
            Root<UserRole> from = criteriaQuery.from(UserRole.class);
            criteriaQuery.where(criteriaBuilder.equal(from.get("name"), nameExpression));
            Query<UserRole> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(nameExpression, name);
            return query.getSingleResult();
        }
    }
}
