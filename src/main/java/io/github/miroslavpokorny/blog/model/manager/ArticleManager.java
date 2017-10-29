package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.ArticleRating;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;

import java.util.List;
import java.util.Map;

public interface ArticleManager {
    List<Article> getAllArticlesByUserId(int userId);
    Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage);
    Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage, Integer gallery);
    Article updateArticle(Article article);
    Article updateArticleById(int id, String name, String content, int category, boolean visible, String previewImage);
    Article updateArticleById(int id, String name, String content, int category, boolean visible, String previewImage, Integer gallery);
    Article getArticleById(int id);
    void deleteArticleById(int id);
    PaginationHelper<Article> getNewestArticles(int page, int itemsPerPage);
    Double getArticleRatingByArticleId(int id);
    Double getArticleRatingByArticleIdAndUserId(int articleId, int userId);
    ArticleRating addOrUpdateRating(int articleId, int userId, double rating);
    PaginationHelper<Article> getNewestArticlesInCategory(int categoryId, int page, int itemsPerPage);
    PaginationHelper<Article> getNewestArticlesByUserId(int userId, int page, int itemsPerPage);
    PaginationHelper<Article> getArticlesSearch(String query, int page, int itemsPerPage);
    Map<Integer, Double> getArticlesRating(List<Integer> articleIds);
}
