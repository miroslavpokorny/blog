package io.github.miroslavpokorny.blog.model.viewmodel;

import java.util.List;

public class HomeViewModel {
    List<ArticleInfoViewModel> latestArticles;

    Integer page;

    Integer numberOfPages;

    public List<ArticleInfoViewModel> getLatestArticles() {
        return latestArticles;
    }

    public void setLatestArticles(List<ArticleInfoViewModel> latestArticles) {
        this.latestArticles = latestArticles;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
