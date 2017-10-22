package io.github.miroslavpokorny.blog.model;

import javax.persistence.*;

//TODO add nullable and foreign keys
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "Name", columnDefinition = "VARCHAR(55)")
    private String name;

    @Column(name = "Description", columnDefinition = "VARCHAR(255)")
    private String description;
}
