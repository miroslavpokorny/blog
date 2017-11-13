package io.github.miroslavpokorny.blog.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ArticleRating {
    @Id
    private int id;

    private Article article;

    private User user;

    private float rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
