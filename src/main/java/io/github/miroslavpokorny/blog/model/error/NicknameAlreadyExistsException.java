package io.github.miroslavpokorny.blog.model.error;

public class NicknameAlreadyExistsException extends RuntimeException {
    public NicknameAlreadyExistsException() {
    }

    public NicknameAlreadyExistsException(String message) {
        super(message);
    }

    public NicknameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NicknameAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public NicknameAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
