package io.github.miroslavpokorny.blog.model.viewmodel;

import java.util.List;

public class HomeViewModel extends PaginationViewModel {
    List<ArticleInfoViewModel> latestArticles;

    List<CategoryInfoViewModel> categories;

    public List<ArticleInfoViewModel> getLatestArticles() {
        return latestArticles;
    }

    public void setLatestArticles(List<ArticleInfoViewModel> latestArticles) {
        this.latestArticles = latestArticles;
    }

    public List<CategoryInfoViewModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInfoViewModel> categories) {
        this.categories = categories;
    }
}
