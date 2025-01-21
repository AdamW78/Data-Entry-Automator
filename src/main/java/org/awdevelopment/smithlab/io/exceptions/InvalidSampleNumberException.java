package org.awdevelopment.smithlab.io.exceptions;

public class InvalidSampleNumberException extends RuntimeException {
    public InvalidSampleNumberException(String value, int row, int column) {
        super("Failed to read cell with value \"" + value + "\" as a sample number at row " + row + " and column " + column);
    }
}
