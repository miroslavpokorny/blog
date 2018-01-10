package io.github.miroslavpokorny.blog.model.json;

public class ChangeUserRoleDto extends JsonBase {
    private int role;
    private int userId;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
