package io.github.miroslavpokorny.blog.model.error;

public class ForbiddenError extends RuntimeException {
    public ForbiddenError() {
    }

    public ForbiddenError(String message) {
        super(message);
    }

    public ForbiddenError(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenError(Throwable cause) {
        super(cause);
    }

    public ForbiddenError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
