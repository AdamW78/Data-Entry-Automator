package org.awdevelopment.smithlab.io.input.exceptions;

public class InvalidBaselineValueException extends RuntimeException {
    public InvalidBaselineValueException(String value, int row, int column) {
        super("Invalid baseline value: \"" + value + "\" at row: " + row + " column: " + column);
    }
}
