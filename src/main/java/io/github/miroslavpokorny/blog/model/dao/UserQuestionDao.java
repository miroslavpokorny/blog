package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.UserQuestion;
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
public class UserQuestionDao extends DaoBase<UserQuestion> implements IUserQuestionDao {
    private final IUserDao userDao;

    @Autowired
    public UserQuestionDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserQuestion> getAllQuestionsByUserId(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<User> userIdExpression = criteriaBuilder.parameter(User.class);
            CriteriaQuery<UserQuestion> criteriaQuery = criteriaBuilder.createQuery(UserQuestion.class);
            Root<UserQuestion> from = criteriaQuery.from(UserQuestion.class);
            criteriaQuery.where(criteriaBuilder.equal(from.get("user"), userIdExpression));
            Query<UserQuestion> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(userIdExpression, userDao.loadById(id));
            return query.getResultList();
        }
    }
}
