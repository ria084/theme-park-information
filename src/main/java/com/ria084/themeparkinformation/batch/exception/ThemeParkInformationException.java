package com.ria084.themeparkinformation.batch.exception;

public class ThemeParkInformationException extends Exception {
    public ThemeParkInformationException(String message) {
        super(message);
    }

    public ThemeParkInformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThemeParkInformationException(Throwable cause) {
        super(cause);
    }

}
