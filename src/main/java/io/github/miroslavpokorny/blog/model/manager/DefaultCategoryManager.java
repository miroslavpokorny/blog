package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCategoryManager implements CategoryManager {
    private final CategoryDao categoryDao;

    @Autowired
    public DefaultCategoryManager(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.getAll();
    }

    @Override
    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return createCategory(category);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryDao.create(category);
    }

    @Override
    public void deleteCategoryByID(int id) {
        categoryDao.delete(categoryDao.loadById(id));
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryDao.getById(id);
    }

    @Override
    public void updateCategory(int id, String name, String description) {
        Category category = categoryDao.getById(id);
        category.setName(name);
        category.setDescription(description);
        updateCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.update(category);
    }
}
