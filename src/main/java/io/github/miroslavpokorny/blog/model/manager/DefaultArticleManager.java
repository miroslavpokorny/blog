package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.ArticleRating;
import io.github.miroslavpokorny.blog.model.dao.*;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultArticleManager implements ArticleManager {

    private final ArticleDao articleDao;

    private final UserDao userDao;

    private final CategoryDao categoryDao;

    private final GalleryDao galleryDao;

    private final ArticleRatingDao articleRatingDao;

    @Autowired
    public DefaultArticleManager(ArticleDao articleDao, UserDao userDao, CategoryDao categoryDao, GalleryDao galleryDao, ArticleRatingDao articleRatingDao) {
        this.articleDao = articleDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.galleryDao = galleryDao;
        this.articleRatingDao = articleRatingDao;
    }

    @Override
    public List<Article> getAllArticlesByUserId(int userId) {
        return articleDao.getAllByUserId(userId);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleDao.getAllArticles();
    }

    @Override
    public Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage) {
        return createArticle(name, content, author, category, visible, previewImage, null);
    }

    @Override
    public Article createArticle(String name, String content, int author, int category, boolean visible, String previewImage, Integer gallery) {
        Article article = new Article();
        article.setName(name);
        article.setContent(content);
        article.setAuthor(userDao.loadById(author));
        article.setCategory(categoryDao.loadById(category));
        article.setVisible(visible);
        article.setPreviewImage(previewImage);
        if (gallery != null) {
            article.setGallery(galleryDao.loadById(gallery));
        }
        article.setPublishDate(new Date());
        article.setEditDate(new Date());
        return createArticle(article);
    }

    @Override
    public Article createArticle(Article article) {
        return articleDao.create(article);
    }

    @Override
    public void updateArticle(Article article) {
        articleDao.update(article);
    }

    @Override
    public void updateArticle(int id, String name, String content, int category, boolean visible, String previewImage) {
        updateArticle(id, name, content, category, visible, previewImage, null);
    }

    @Override
    public void updateArticle(int id, String name, String content, int category, boolean visible, String previewImage, Integer gallery) {
        Article article = articleDao.getById(id);
        article.setName(name);
        article.setContent(content);
        article.setCategory(categoryDao.loadById(category));
        article.setVisible(visible);
        article.setPreviewImage(previewImage);
        article.setGallery(gallery != null ? galleryDao.loadById(gallery) : null);
        updateArticle(article);
    }

    @Override
    public Article getArticleById(int id) {
        return articleDao.getById(id);
    }

    @Override
    public void deleteArticleById(int id) {
        Article article = articleDao.loadById(id);
        articleDao.delete(article);
    }

    @Override
    public PaginationHelper<Article> getNewestArticles(int page, int itemsPerPage) {
        return articleDao.getNewestArticles(page, itemsPerPage);
    }

    @Override
    public Double getArticleRatingByArticleId(int id) {
        return (double) articleRatingDao.getAvgRatingByArticleId(id);
    }

    @Override
    public Double getArticleRatingByArticleIdAndUserId(int articleId, int userId) {
        return (double) articleRatingDao.getArticleRatingByArticleIdAndUserId(articleId, userId).getRating();
    }

    @Override
    public void addOrUpdateRating(int articleId, int userId, double rating) {
        ArticleRating articleRating = new ArticleRating();
        articleRating.setArticle(articleDao.loadById(articleId));
        articleRating.setUser(userDao.loadById(userId));
        articleRating.setRating((float)rating);
        articleRatingDao.saveOrUpdate(articleRating);
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesInCategory(int categoryId, int page, int itemsPerPage) {
        return articleDao.getNewestArticlesInCategory(page, itemsPerPage, categoryId);
    }

    @Override
    public PaginationHelper<Article> getNewestArticlesByUserId(int userId, int page, int itemsPerPage) {
        return articleDao.getNewestArticlesByUserId(page, itemsPerPage, userId);
    }

    @Override
    public PaginationHelper<Article> getArticlesSearch(String query, int page, int itemsPerPage) {
        return articleDao.getArticlesSearch(page, itemsPerPage, query);
    }

    @Override
    public Map<Integer, Double> getArticlesRating(List<Integer> articleIds) {
        List<ArticleRating> ratings = articleRatingDao.getAvgRatingForArticles(articleIds);
        Map<Integer, Double> result = new HashMap<>();
        for (ArticleRating rating : ratings) {
            result.put(rating.getArticle().getId(), (double)rating.getRating());
        }
        return result;
    }
}
