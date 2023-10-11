package com.github.cleydyr.dart.system.io.exception;

import java.io.IOException;

@SuppressWarnings("serial")
public class ReleaseDownloadException extends IOException {

    public ReleaseDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReleaseDownloadException(String message) {
        super(message);
    }
}
