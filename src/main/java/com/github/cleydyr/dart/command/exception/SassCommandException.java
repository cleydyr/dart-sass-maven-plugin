package com.github.cleydyr.dart.command.exception;

@SuppressWarnings("serial")
public class SassCommandException extends Exception {
    public SassCommandException(String message) {
        super(message);
    }

    public SassCommandException(Exception cause) {
        super(cause);
    }

    public SassCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
