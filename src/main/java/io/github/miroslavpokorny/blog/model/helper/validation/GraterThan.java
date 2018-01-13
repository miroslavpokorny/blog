package io.github.miroslavpokorny.blog.model.helper.validation;

public class GraterThan implements ValidationRule {
    private final String value;

    private final int min;

    GraterThan(String value, int min) {
        this.value = value;
        this.min = min;
    }

    @Override
    public boolean isValid() {
        try {
            return Integer.parseInt(this.value) > min;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
