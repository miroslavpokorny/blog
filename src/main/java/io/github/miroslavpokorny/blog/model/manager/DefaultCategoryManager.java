package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Category;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class DefaultCategoryManager implements CategoryManager {
    @Override
    public List<Category> getAllCategories() {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Category createCategory(String name, String description) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Category createCategory(Category category) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public void deleteCategoryByID(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Category getCategoryById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Category updateCategory(int id, String name, String description) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Category updateCategory(Category category) {
        //TODO implement
        throw new NotImplementedException();
    }
}
