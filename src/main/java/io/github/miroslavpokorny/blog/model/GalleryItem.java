package io.github.miroslavpokorny.blog.model;

//TODO add nullable and foreign keys

import javax.persistence.*;

@Entity
@Table(name = "GalleryItem")
public class GalleryItem {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "ImageName", columnDefinition = "VARCHAR(55)")
    private String imageName;

    @Column(name = "Title", columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "Gallery", columnDefinition = "INT")
    private Gallery gallery;
}
