package io.github.miroslavpokorny.blog.model;

//TODO add nullable and foreign keys

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "Id", columnDefinition = "INT")
    private int id;

    @Column(name = "Name", columnDefinition = "VARCHAR(55)")
    private String name;

    @Column(name = "Surname", columnDefinition = "VARCHAR(55)")
    private String surname;

    @Column(name = "Email", columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "Password", columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "Nickname", columnDefinition = "VARCHAR(55)")
    private String nickname;

    @Column(name = "LastSignInDate", columnDefinition = "DATETIME")
    private Date lastSignInDate;

    @Column(name = "Role", columnDefinition = "INT")
    private UserRole role;

    @Column(name = "Enabled", columnDefinition = "BIT(1)")
    private boolean enabled;

    @Column(name = "Avatar", columnDefinition = "VARCHAR(255)")
    private String avatar;

    @Column(name = "RestorePasswordKey", columnDefinition = "VARCHAR(255)")
    private String restorePasswordKey;

    @Column(name = "ActivationEmailKey", columnDefinition = "VARCHAR(255)")
    private String activationEmailKey;

    @Column(name = "Activated", columnDefinition = "BIT(1)")
    private boolean activated;
}
