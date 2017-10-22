package io.github.miroslavpokorny.blog.model;

//TODO add nullable and foreign keys

import javax.persistence.*;

@Entity
@Table(name = "UserRole")
public class UserQuestion {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "User", columnDefinition = "INT")
    private User user;

    @Column(name = "Question", columnDefinition = "VARCHAR(255)")
    private String question;

    @Column(name = "Answer", columnDefinition = "VARCHAR(255)")
    private String answer;
}
