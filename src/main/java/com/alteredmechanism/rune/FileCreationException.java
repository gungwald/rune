package com.alteredmechanism.rune;

import java.io.File;

public class FileCreationException extends Throwable {
    public FileCreationException(String message, File file) {
        super(message + ": " + file.getAbsolutePath());
    }
}
