package com.yujunyang.intellij.plugin.sonar.common.exceptions;

public class ApiRequestFailedException extends Exception {
    public ApiRequestFailedException() {
        super();
    }

    public ApiRequestFailedException(String msg) {
        super(msg);
    }

    public ApiRequestFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ApiRequestFailedException(Throwable cause) {
        super(cause);
    }
}
