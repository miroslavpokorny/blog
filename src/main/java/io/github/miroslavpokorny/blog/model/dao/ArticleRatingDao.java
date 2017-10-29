package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.ArticleRating;

public interface ArticleRatingDao extends Dao<ArticleRating> {
    // void getAvgRatingForArticles(List<Integer> articleIds);

    float getAvgRatingForArticleById(int id);

    ArticleRating getArticleRatingByArticleIdAndUserId(int articleId, int userId);
}
