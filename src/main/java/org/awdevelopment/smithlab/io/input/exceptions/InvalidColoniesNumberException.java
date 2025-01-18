package org.awdevelopment.smithlab.io.input.exceptions;

public class InvalidColoniesNumberException extends RuntimeException {
    public InvalidColoniesNumberException(String value, int row, int column) {
        super("Failed to read cell with value \"" + value + "\" as a colonies number at row " + row + " and column " + column);
    }
}
