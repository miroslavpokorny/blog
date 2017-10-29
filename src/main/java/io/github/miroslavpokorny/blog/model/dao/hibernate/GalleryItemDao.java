package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.dao.GalleryDao;
import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GalleryItemDao extends  DaoBase<GalleryItem> implements io.github.miroslavpokorny.blog.model.dao.GalleryItemDao {
    private final GalleryDao galleryDao;

    @Autowired
    public GalleryItemDao(GalleryDao galleryDao) {
        this.galleryDao = galleryDao;
    }

    @Override
    public void addImagesToGallery(List<GalleryItem> items) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            Transaction transaction = null;
            try {
                transaction = session.delegate().beginTransaction();
                for (GalleryItem item : items) {
                    saveOrUpdate(item);
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null) transaction.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<GalleryItem> getAllItemsInGallery(int galleryId) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Gallery> galleryIdExpression = criteriaBuilder.parameter(Gallery.class);
            CriteriaQuery<GalleryItem> criteriaQuery = criteriaBuilder.createQuery(GalleryItem.class);
            Root<GalleryItem> from = criteriaQuery.from(GalleryItem.class);
            criteriaQuery.where(criteriaBuilder.equal(from.get("gallery"), galleryIdExpression));
            Query<GalleryItem> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(galleryIdExpression, galleryDao.loadById(galleryId));
            return query.getResultList();
        }
    }

    @Override
    public GalleryItem getGalleryItemById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            CriteriaBuilder criteriaBuilder = session.delegate().getCriteriaBuilder();
            ParameterExpression<Integer> galleryItemIdExpression = criteriaBuilder.parameter(Integer.class);
            CriteriaQuery<GalleryItem> criteriaQuery = criteriaBuilder.createQuery(GalleryItem.class);
            Root<GalleryItem> from = criteriaQuery.from(GalleryItem.class);
            from.fetch("gallery");
            criteriaQuery.where(criteriaBuilder.equal(from.get("id"), galleryItemIdExpression));
            Query<GalleryItem> query = session.delegate().createQuery(criteriaQuery);
            query.setParameter(galleryItemIdExpression, id);
            return query.getSingleResult();
        }
    }
}
