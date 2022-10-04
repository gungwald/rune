package com.alteredmechanism.izpack;

public class InvalidInstallPathException extends Exception {
    public InvalidInstallPathException(String message) {
        super(message);
    }

    public InvalidInstallPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
