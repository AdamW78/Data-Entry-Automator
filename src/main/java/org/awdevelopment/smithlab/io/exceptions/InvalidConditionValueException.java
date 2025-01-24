package org.awdevelopment.smithlab.io.exceptions;

public class InvalidConditionValueException extends InputFileWarningException {
    public InvalidConditionValueException(String value, int row, int column) {
        super("Invalid condition value: \"" + value + "\" at row: " + row + " column: " + column + ". Must be a non-empty string.");
    }
}
