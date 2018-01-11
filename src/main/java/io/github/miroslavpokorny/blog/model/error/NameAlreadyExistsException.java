package io.github.miroslavpokorny.blog.model.error;

public class NameAlreadyExistsException extends RuntimeException  {

    public NameAlreadyExistsException() {
        super();
    }

    public NameAlreadyExistsException(String message) {
        super(message);
    }

    public NameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected NameAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
