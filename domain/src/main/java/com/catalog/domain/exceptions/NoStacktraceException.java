package com.catalog.domain.exceptions;

public class NoStacktraceException extends RuntimeException {
    public NoStacktraceException(final String message, Throwable cause) {
        super(message, cause, true, false);
    }

    public NoStacktraceException(final String message) {
        this(message, null);
    }
}
