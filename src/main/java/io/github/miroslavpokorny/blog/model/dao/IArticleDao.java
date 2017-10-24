package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;

import java.util.List;

public interface IArticleDao extends IDao<Article> {
    List<Article> getAllByUserId(int id);

    PaginationHelper<Article> getNewestArticles(int page, int itemsPerPage);

    PaginationHelper<Article> getNewestArticlesInCategory(int page, int itemsPerPage, int categoryId);

    PaginationHelper<Article> getNewestArticlesByUserId(int page, int itemsPerPage, int userId);

    PaginationHelper<Article> getArticlesSearch(int page, int itemsPerPage, String searchQuery);
}
