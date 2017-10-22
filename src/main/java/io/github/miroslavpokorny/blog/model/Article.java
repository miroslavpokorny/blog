package io.github.miroslavpokorny.blog.model;

import javax.persistence.*;
import java.util.Date;

//TODO add nullable and foreign keys
@Entity
@Table(name = "Article")
public class Article {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "Name", columnDefinition = "VARCHAR(55)")
    private String name;

    @Column(name = "PreviewImage", columnDefinition = "VARCHAR(55)")
    private String previewImage;

    @Column(name = "Content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "Author", columnDefinition = "INT")
    private User author;

    @Column(name = "PublishDate", columnDefinition = "DATETIME")
    private Date publishDate;

    @Column(name = "EditDate", columnDefinition = "DATETIME")
    private Date editDate;

    @Column(name = "Category", columnDefinition = "INT")
    private Category category;

    @Column(name = "Visible", columnDefinition = "BIT(1)")
    private boolean visible;

    @Column(name = "Gallery", columnDefinition = "INT")
    private Gallery gallery;
}
