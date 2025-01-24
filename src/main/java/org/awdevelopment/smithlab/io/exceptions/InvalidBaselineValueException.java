package org.awdevelopment.smithlab.io.exceptions;

public class InvalidBaselineValueException extends InputFileWarningException {
    public InvalidBaselineValueException(String value, int row, int column) {
        super("Invalid baseline value: \"" + value + "\" at row: " + row + " column: " + column);
    }
}
