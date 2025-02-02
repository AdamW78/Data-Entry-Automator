package org.awdevelopment.smithlab.io.exceptions;


import java.io.IOException;

public class InvalidSampleNumberException extends IOException {
    public InvalidSampleNumberException(String value, int row, int column) {
        super("Failed to read sample number from cell with value \"" + value + "\" as a sample number at row "
                + row + " and column " + column + " - Skipping this row...");
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    private String getDisplayName() {
        return "Invalid Sample Number";
    }
}
