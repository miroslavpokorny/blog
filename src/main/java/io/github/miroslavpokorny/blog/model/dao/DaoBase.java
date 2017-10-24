package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.helper.CloseableSession;
import io.github.miroslavpokorny.blog.model.helper.HibernateHelper;
import io.github.miroslavpokorny.blog.model.helper.ListHelper;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.hibernate.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.List;

abstract public class DaoBase<T> implements IDao<T> {
    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public DaoBase() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public List<T> getAll() {
        try (CloseableSession session = HibernateHelper.getSession()) {
            return ListHelper.listToGeneric(session.delegate().createQuery(String.format("from %s", getPersistentClass().getTypeName())).list());
        }
    }

    @Override
    public PaginationHelper<T> getAllWithPagination(int page, int itemsPerPage) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            PaginationHelper<T> result = new PaginationHelper<T>();
            result.setPage(page);
            result.setItemsPerPage(itemsPerPage);
            Query query = session.delegate().createQuery(String.format("from %s", getPersistentClass().getTypeName()));
            query.setFirstResult((page - 1) * itemsPerPage);
            query.setFetchSize(itemsPerPage);
            result.setItems(ListHelper.listToGeneric(query.list()));
            int total = ((Long)session.delegate().createQuery(String.format("select count(*) from %s", getPersistentClass().getTypeName())).uniqueResult()).intValue();
            result.setTotal(total);
            return result;
        }
    }
//
//    protected PaginationHelper<T> getAllWithPaginationByParam(int page, int itemsPerPage, String paramName, Object paramValue) {
//        try (CloseableSession session = HibernateHelper.getSession()) {
//            PaginationHelper<T> result = new PaginationHelper<T>();
//            result.setPage(page);
//            result.setItemsPerPage(itemsPerPage);
//            Query query = session.delegate().createQuery(String.format("from %s where %s = :param", getPersistentClass().getTypeName(), paramName));
//            query.setFirstResult((page - 1) * itemsPerPage);
//            query.setFetchSize(itemsPerPage);
//            query.setParameter("param", paramValue);
//            result.setItems(ListHelper.listToGeneric(query.list()));
//            Query totalQuery = session.delegate().createQuery(String.format("select count(*) from %s where %s = :param", getPersistentClass().getTypeName(), paramName));
//            totalQuery.setParameter("param", paramValue);
//            result.setTotal(((Long)totalQuery.uniqueResult()).intValue());
//            // int total = ((Long)session.delegate().createQuery(String.format("select count(*) from %s", getPersistentClass().getTypeName(), paramName)).uniqueResult()).intValue();
//            // result.setTotal(total);
//            return result;
//        }
//    }

    @Override
    public void create(T entity) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            try {
                session.delegate().getTransaction().begin();
                session.delegate().save(entity);
                session.delegate().getTransaction().commit();
            } catch (Exception e) {
                session.delegate().getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public void update(T entity) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            try {
                session.delegate().getTransaction().begin();
                session.delegate().update(entity);
                session.delegate().getTransaction().commit();
            } catch (Exception e) {
                session.delegate().getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(T entity) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            session.delegate().flush();
            try {
                session.delegate().getTransaction().begin();
                session.delegate().delete(entity);
                session.delegate().getTransaction().commit();
            } catch (Exception e) {
                session.delegate().getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            return (T)session.delegate().get(persistentClass.getTypeName(), id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T loadById(int id) {
        try (CloseableSession session = HibernateHelper.getSession()) {
            return (T) session.delegate().load(persistentClass.getTypeName(), id);
        }
    }

    protected PaginationHelper<T> createPaginationHelper(int page, int itemsPerPage, Query<T> itemsQuery, Query<Long> countQuery) {
        PaginationHelper<T> result = new PaginationHelper<>();
        result.setPage(page);
        result.setItemsPerPage(itemsPerPage);
        result.setItems(itemsQuery.getResultList());
        result.setTotal(countQuery.getSingleResult());
        return result;
    }
}
