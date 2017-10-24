package io.github.miroslavpokorny.blog.model;

import java.util.Date;

public class User {
    private int id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private String nickname;

    private Date lastSignInDate;

    private UserRole role;

    private boolean enabled;

    private String avatar;

    private String restorePasswordKey;

    private String activationEmailKey;

    private boolean activated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLastSignInDate() {
        return lastSignInDate;
    }

    public void setLastSignInDate(Date lastSignInDate) {
        this.lastSignInDate = lastSignInDate;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRestorePasswordKey() {
        return restorePasswordKey;
    }

    public void setRestorePasswordKey(String restorePasswordKey) {
        this.restorePasswordKey = restorePasswordKey;
    }

    public String getActivationEmailKey() {
        return activationEmailKey;
    }

    public void setActivationEmailKey(String activationEmailKey) {
        this.activationEmailKey = activationEmailKey;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
