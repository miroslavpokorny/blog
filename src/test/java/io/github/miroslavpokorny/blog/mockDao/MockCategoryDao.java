package io.github.miroslavpokorny.blog.mockDao;

import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.dao.CategoryDao;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Primary
@Repository
@Profile("TEST")
public class MockCategoryDao implements CategoryDao {
    private List<Category> categories;

    public MockCategoryDao() {
        categories = new ArrayList<>();
        categories.add(new Category(1, "Category one", "First category"));
        categories.add(new Category(2, "Category two", "Second category"));
        categories.add(new Category(3, "Category three", null));
        categories.add(new Category(4, "ToDelete1", null));
        categories.add(new Category(5, "ToDelete2", null));
        categories.add(new Category(6, "ToDelete3", null));
        categories.add(new Category(7, "ToEdit1", null));
        categories.add(new Category(8, "ToEdit2", null));
        categories.add(new Category(9, "ToEdit3", null));
    }

    @Override
    public List<Category> getAll() {
        return categories;
    }

    @Override
    public PaginationHelper<Category> getAllWithPagination(int page, int itemsPerPage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Category create(Category entity) {
        Category sameName = categories.stream().filter(category -> category.getName().equals(entity.getName())).findFirst().orElse(null);
        if (sameName != null) {
            throw new ConstraintViolationException("Category with specified name already exist", null, "Name_UNIQUE");
        }
        int maxId = categories.stream().max(Comparator.comparingInt(Category::getId)).orElseGet(() -> new Category(1, null, null)).getId();
        entity.setId(maxId + 1);
        categories.add(entity);
        return entity;
    }

    @Override
    public void update(Category entity) {
        Category toEdit = categories.stream().filter(category -> category.getId() == entity.getId()).findFirst().orElse(null);
        if (toEdit == null) {
            return;
        }
        categories.remove(toEdit);
        categories.add(entity);
    }

    @Override
    public void delete(Category entity) {
        Category toDelete = categories.stream().filter(category -> category.getId() == entity.getId()).findFirst().orElse(null);
        if (toDelete == null) {
            return;
        }
        categories.remove(toDelete);
    }

    @Override
    public void saveOrUpdate(Category entity) {
        categories.stream().filter(category -> category.getId() == entity.getId()).findFirst().ifPresent(toEdit -> categories.remove(toEdit));
        categories.add(entity);
    }

    @Override
    public Category getById(int id) {
        return categories.stream().filter(category -> category.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Category loadById(int id) {
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
