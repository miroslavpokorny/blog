package io.github.miroslavpokorny.blog.model.json;

public abstract class JsonBase {
    private String type = this.getClass().getSimpleName();

    public String getType() {
        return type;
    }
}
