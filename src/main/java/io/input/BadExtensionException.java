package io.input;

public class BadExtensionException extends IllegalArgumentException {

    public BadExtensionException(String extension) {
        super("Error: \"" + extension + "\" is not a valid extension - only .xlsx files are supported.");
    }
}
