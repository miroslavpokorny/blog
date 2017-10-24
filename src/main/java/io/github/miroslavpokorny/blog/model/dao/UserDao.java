package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import io.github.miroslavpokorny.blog.model.User;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

public class UserDao extends DaoBase<User> implements IUserDao {
    public UserDao() {
        super();
    }

    @Override
    public User getByNickname(String nickname) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<String> nicknameExpression = criteriaBuilder.parameter(String.class);
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> from = criteriaQuery.from(User.class);
            from.fetch("role");
            criteriaQuery.where(criteriaBuilder.equal(from.get("nickname"), nicknameExpression));
            Query<User> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(nicknameExpression, nickname);
            return query.getSingleResult();
        }
    }

    @Override
    public User getByEmail(String email) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<String> emailExpression = criteriaBuilder.parameter(String.class);
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> from = criteriaQuery.from(User.class);
            from.fetch("role");
            criteriaQuery.where(criteriaBuilder.equal(from.get("email"), emailExpression));
            Query<User> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(emailExpression, email);
            return query.getSingleResult();
        }
    }
}
