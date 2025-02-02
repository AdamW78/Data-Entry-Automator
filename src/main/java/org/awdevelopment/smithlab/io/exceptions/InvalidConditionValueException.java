package org.awdevelopment.smithlab.io.exceptions;

import java.io.IOException;

public class InvalidConditionValueException extends IOException {
    public InvalidConditionValueException(String value, int row, int column) {
        super("Invalid condition value: \"" + value + "\" at row: " + row + " column: " + column + ". Must be a non-empty string.");
    }

    @Override
    public String toString() { return getDisplayName();}

    private String getDisplayName() { return "Invalid Condition Value"; }
}
