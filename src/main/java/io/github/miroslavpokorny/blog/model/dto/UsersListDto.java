package io.github.miroslavpokorny.blog.model.dto;

import java.util.List;

public class UsersListDto extends DtoBase {
    private List<UserInfoDto> users;

    public List<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
    }
}
