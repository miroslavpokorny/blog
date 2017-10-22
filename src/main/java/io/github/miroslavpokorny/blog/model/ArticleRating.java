package io.github.miroslavpokorny.blog.model;

import javax.persistence.*;

//TODO add nullable and foreign keys
@Entity
@Table(name = "ArticleRating")
public class ArticleRating {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "Article", columnDefinition = "INT")
    private Article article;

    @Column(name = "User", columnDefinition = "INT")
    private User user;

    @Column(name = "Rating", columnDefinition = "FLOAT")
    private float rating;
}
