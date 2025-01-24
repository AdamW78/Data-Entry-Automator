package org.awdevelopment.smithlab.io.exceptions;

import java.io.IOException;

public class OutputException extends IOException {
    protected OutputException(String message) {
        super(message);
    }

    public OutputException(String filename, Exception e) {
        super("Error writing to file: " + filename + " - " + e.getMessage());
    }
}
