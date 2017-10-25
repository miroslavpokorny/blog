package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.ArticleRating;

import java.util.List;

public interface IArticleRatingDao extends IDao<ArticleRating> {
    // void getAvgRatingForArticles(List<Integer> articleIds);

    float getAvgRatingForArticleById(int id);

    ArticleRating getArticleRatingByArticleIdAndUserId(int articleId, int userId);
}
