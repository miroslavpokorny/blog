package io.github.miroslavpokorny.blog.model.json;

import java.util.List;

public class UsersListDto extends JsonBase {
    private List<UserInfoDto> users;

    public List<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
    }
}
