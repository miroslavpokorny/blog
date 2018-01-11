package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class GalleryDao extends DaoBase<Gallery> implements io.github.miroslavpokorny.blog.model.dao.GalleryDao {
    private final UserDao userDao;

    @Autowired
    public GalleryDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<Gallery> getGalleriesByUserId(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<User> userExpression = criteriaBuilder.parameter(User.class);
            CriteriaQuery<Gallery> criteriaQuery = criteriaBuilder.createQuery(Gallery.class);
            Root<Gallery> from = criteriaQuery.from(Gallery.class);
            from.fetch("Author");
            criteriaQuery.where(criteriaBuilder.equal(from.get("Author"), userExpression));
            Query<Gallery> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(userExpression, userDao.loadById(id));
            return query.getResultList();
        }
    }

    @Override
    public List<Gallery> getAllGalleries() {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            CriteriaQuery<Gallery> criteriaQuery = criteriaBuilder.createQuery(Gallery.class);
            Root<Gallery> from = criteriaQuery.from(Gallery.class);
            from.fetch("Author");
            Query<Gallery> query = session.delegate().createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    @Override
    public Gallery getGalleryById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Integer> galleryIdExpression = criteriaBuilder.parameter(Integer.class);
            CriteriaQuery<Gallery> criteriaQuery = criteriaBuilder.createQuery(Gallery.class);
            Root<Gallery> from = criteriaQuery.from(Gallery.class);
            from.fetch("Author");
            criteriaQuery.where(criteriaBuilder.equal(from.get("id"), galleryIdExpression));
            Query<Gallery> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(galleryIdExpression, id);
            return query.getSingleResult();
        }
    }
}
