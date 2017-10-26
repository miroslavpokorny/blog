package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GalleryDao extends DaoBase<Gallery> implements IGalleryDao {
    private final IUserDao userDao;

    @Autowired
    public GalleryDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<Gallery> getGalleriesByUserId(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<User> userExpression = criteriaBuilder.parameter(User.class);
            CriteriaQuery<Gallery> criteriaQuery = criteriaBuilder.createQuery(Gallery.class);
            Root<Gallery> from = criteriaQuery.from(Gallery.class);
            from.fetch("author");
            criteriaQuery.where(criteriaBuilder.equal(from.get("author"), userExpression));
            Query<Gallery> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(userExpression, userDao.loadById(id));
            return query.getResultList();
        }
    }

    @Override
    public List<Gallery> getAllGalleries() {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery getGalleryById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
