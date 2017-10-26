package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;

import java.util.List;

public interface IDao<T> {
    List<T> getAll();
    PaginationHelper<T> getAllWithPagination(int page, int itemsPerPage);
    void create(T entity);
    void update(T entity);
    void delete(T entity);
    void saveOrUpdate(T entity);
    T getById(int id);
    T loadById(int id);
}
