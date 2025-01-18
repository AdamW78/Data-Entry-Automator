package io.output;

import java.io.IOException;

public class OutputException extends IOException {
    public OutputException(String filename, Exception e) {
        super("Error writing to file: " + filename + " - " + e.getMessage());
    }
}
