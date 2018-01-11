package io.github.miroslavpokorny.blog.model.dto;

public abstract class DtoBase {
    private String type = this.getClass().getSimpleName();

    public String getType() {
        return type;
    }
}
