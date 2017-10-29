package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Category;

import java.util.List;

public interface CategoryManager {
    List<Category> getAllCategories();
    Category createCategory(String name, String description);
    Category createCategory(Category category);
    void deleteCategoryByID(int id);
    Category getCategoryById(int id);
    Category updateCategory(int id, String name, String description);
    Category updateCategory(Category category);
}
