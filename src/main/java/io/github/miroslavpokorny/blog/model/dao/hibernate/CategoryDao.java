package io.github.miroslavpokorny.blog.model.dao.hibernate;

import io.github.miroslavpokorny.blog.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends DaoBase<Category> implements io.github.miroslavpokorny.blog.model.dao.CategoryDao {
}
