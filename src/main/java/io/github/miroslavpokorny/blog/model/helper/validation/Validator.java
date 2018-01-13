package io.github.miroslavpokorny.blog.model.helper.validation;

import java.util.ArrayList;
import java.util.List;

public class Validator {
    private final List<ValidationRule> validations = new ArrayList<>();

    private String value;

    public static Validator notEmpty(String value) {
        Validator validator = new Validator();
        validator.value = value;
        validator.notEmpty();
        return validator;
    }

    public static Validator email(String value) {
        Validator validator = new Validator();
        validator.value = value;
        validator.email();
        return validator;
    }

    public static Validator sameAs(String value, String sameAs) {
        Validator validator = new Validator();
        validator.value = value;
        validator.sameAs(sameAs);
        return validator;
    }

    public static Validator graterThan(int value, int min) {
        Validator validator = new Validator();
        validator.value = String.valueOf(value);
        validator.graterThan(min);
        return validator;
    }

    public static Validator graterThan(String value, int min) {
        Validator validator = new Validator();
        validator.value = value;
        validator.graterThan(min);
        return validator;
    }

    public Validator notEmpty() {
        ValidationRule rule = new NotEmpty(value);
        validations.add(rule);
        return this;
    }

    public Validator email() {
        ValidationRule rule = new Email(value);
        validations.add(rule);
        return this;
    }

    public Validator sameAs(String sameAs) {
        ValidationRule rule = new SameAs(value, sameAs);
        validations.add(rule);
        return this;
    }

    public Validator graterThan(int min) {
        ValidationRule rule = new GraterThan(value, min);
        validations.add(rule);
        return this;
    }

    public boolean isValid() {
        for (ValidationRule rule : validations) {
            if (!rule.isValid()) {
                return false;
            }
        }
        return true;
    }
}