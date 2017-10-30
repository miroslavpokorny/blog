package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.ArticleRating;

import java.util.List;

public interface ArticleRatingDao extends Dao<ArticleRating> {
    // void getAvgRatingForArticles(List<Integer> articleIds);

    List<ArticleRating> getAvgRatingForArticles(List<Integer> articleIds);

    float getAvgRatingByArticleId(int id);

    ArticleRating getArticleRatingByArticleIdAndUserId(int articleId, int userId);
}
