package io.github.miroslavpokorny.blog.model.viewmodel;

public class BaseViewModel {
    private boolean authenticated;

    private int role;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
