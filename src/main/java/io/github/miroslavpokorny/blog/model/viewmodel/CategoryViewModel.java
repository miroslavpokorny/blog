package io.github.miroslavpokorny.blog.model.viewmodel;

import java.util.List;

public class CategoryViewModel extends PaginationViewModel {
    private List<ArticleInfoViewModel> articles;

    private List<CategoryInfoViewModel> categories;

    private CategoryInfoViewModel currentCategory;

    public List<ArticleInfoViewModel> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleInfoViewModel> articles) {
        this.articles = articles;
    }

    public List<CategoryInfoViewModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInfoViewModel> categories) {
        this.categories = categories;
    }

    public CategoryInfoViewModel getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(CategoryInfoViewModel currentCategory) {
        this.currentCategory = currentCategory;
    }
}
