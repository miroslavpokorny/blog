package io.github.miroslavpokorny.blog.model;

import javax.persistence.*;
import java.util.Date;

//TODO add nullable and foreign keys

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "Article", columnDefinition = "INT")
    private Article article;

    @Column(name = "ParentComment", columnDefinition = "INT")
    private Comment parentComment;

    @Column(name = "Author", columnDefinition = "INT")
    private User author;

    @Column(name = "PublishDate", columnDefinition = "DATETIME")
    private Date publishDate;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "Visible", columnDefinition = "BIT(1)")
    private boolean visible;

}
