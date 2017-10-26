package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends DaoBase<Category> implements ICategoryDao {
}
