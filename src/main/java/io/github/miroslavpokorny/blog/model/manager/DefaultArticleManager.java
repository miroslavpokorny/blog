package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.ArticleRating;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

@Service
public class DefaultArticleManager implements ArticleManager {
    @Override
    public List<Article> getAllArticlesByUserId(int userId) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage, Integer gallery) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article updateArticle(Article article) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article updateArticleById(int id, String name, String content, int category, boolean visible, String previewImage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article updateArticleById(int id, String name, String content, int category, boolean visible, String previewImage, Integer gallery) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Article getArticleById(int id) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public void deleteArticleById(int id) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public PaginationHelper<Article> getNewestArticles(int page, int itemsPerPage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Double getArticleRatingByArticleId(int id) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Double getArticleRatingByArticleIdAndUserId(int articleId, int userId) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public ArticleRating addOrUpdateRating(int articleId, int userId, double rating) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesInCategory(int categoryId, int page, int itemsPerPage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesByUserId(int userId, int page, int itemsPerPage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public PaginationHelper<Article> getArticlesSearch(String query, int page, int itemsPerPage) {
        // TODO implemented
        throw new NotImplementedException();
    }

    @Override
    public Map<Integer, Double> getArticlesRating(List<Integer> articleIds) {
        // TODO implemented
        throw new NotImplementedException();
    }
}
