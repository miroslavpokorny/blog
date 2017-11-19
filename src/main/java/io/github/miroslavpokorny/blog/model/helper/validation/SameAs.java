package io.github.miroslavpokorny.blog.model.helper.validation;

public class SameAs implements ValidationRule {
    private final String value;

    private final String sameAs;

    SameAs(String value, String sameAs) {
        this.value = value;
        this.sameAs = sameAs;
    }

    @Override
    public boolean isValid() {

        return (this.value == null && this.sameAs == null) || (this.value != null && this.value.equals(this.sameAs));
    }
}
