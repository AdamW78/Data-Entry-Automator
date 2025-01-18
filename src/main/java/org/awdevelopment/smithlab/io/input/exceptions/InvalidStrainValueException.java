package org.awdevelopment.smithlab.io.input.exceptions;

public class InvalidStrainValueException extends RuntimeException {
    public InvalidStrainValueException(String value, int row, int column) {
        super("Invalid strain value: " + value + " at row: " + row + " column: " + column);
    }
}
