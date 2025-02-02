package org.awdevelopment.smithlab.io.exceptions;

import java.io.IOException;

@SuppressWarnings("SameParameterValue")
public abstract class OutputException extends IOException {
    public OutputException(String message) {
        super(message);
    }

    public OutputException(String filename, Exception e) {
        super("Error writing to file: " + filename + " - " + e.getMessage());
    }

    public abstract String getDisplayName();


}
