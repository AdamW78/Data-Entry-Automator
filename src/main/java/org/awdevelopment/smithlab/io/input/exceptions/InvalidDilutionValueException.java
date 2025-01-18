package org.awdevelopment.smithlab.io.input.exceptions;

public class InvalidDilutionValueException extends RuntimeException {
    public InvalidDilutionValueException(String value, int row, int column) {
        super("Invalid dilution value: \"" + value + "\" at row: " + row + " column: " + column + ". Must be one of: 0.01, 0.1, 1");
    }
}
