package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.dao.CategoryDao;
import io.github.miroslavpokorny.blog.model.error.NameAlreadyExistsException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
        try {
            return categoryDao.create(category);
        } catch (ConstraintViolationException exception) {
            if (exception.getConstraintName().equals("Name_UNIQUE")) {
                throw new NameAlreadyExistsException("Category with name '" + category.getName() + "' already exists!", exception);
            }
            throw exception;
        } catch (PersistenceException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintException = (ConstraintViolationException) exception.getCause();
                if (constraintException.getConstraintName().equals("Name_UNIQUE")) {
                    throw new NameAlreadyExistsException("Category with name '" + category.getName() + "' already exists!", exception);
                }
            }
            throw exception;
        }
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
        try {
            categoryDao.update(category);
        } catch (ConstraintViolationException exception) {
            if (exception.getConstraintName().equals("Name_UNIQUE")) {
                throw new NameAlreadyExistsException("Category with name '" + category.getName() + "' already exists!", exception);
            }
            throw exception;
        } catch (PersistenceException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintException = (ConstraintViolationException) exception.getCause();
                if (constraintException.getConstraintName().equals("Name_UNIQUE")) {
                    throw new NameAlreadyExistsException("Category with name '" + category.getName() + "' already exists!", exception);
                }
            }
            throw exception;
        }
    }
}
