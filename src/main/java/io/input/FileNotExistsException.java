package io.input;

public class FileNotExistsException extends IllegalArgumentException {
    public FileNotExistsException(String path) {
        super("Error: File with path: " + path + " does not exist.");
    }
}
