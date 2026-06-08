package com.github.cleydyr.dart.system.exception;

public class OSDetectionException extends RuntimeException {
    public OSDetectionException(String message) {
        super(message);
    }

    public OSDetectionException(Exception cause) {
        super(cause);
    }

    public OSDetectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
