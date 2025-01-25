package org.awdevelopment.smithlab.io.exceptions;

public class InvalidBaselineValueException extends InputFileException {
    public InvalidBaselineValueException(String value, int row, int column) {
        super("Invalid baseline value: \"" + value + "\" at row: " + row + " column: " + column + "." +
                "\n The Smith Lab Data Entry Automator will attempt to determine the correct value.");
    }
}
