package io.github.miroslavpokorny.blog.model.helper.validation;

public class NotEmpty implements ValidationRule {
    private final String value;

    NotEmpty(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid() {
        return (value != null && !value.trim().isEmpty());
    }
}
