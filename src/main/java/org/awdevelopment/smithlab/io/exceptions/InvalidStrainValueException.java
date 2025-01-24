package org.awdevelopment.smithlab.io.exceptions;

public class InvalidStrainValueException extends InputFileWarningException {
    public InvalidStrainValueException(String value, int row, int column) {
        super("Invalid strain value: " + value + " at row: " + row + " column: " + column);
    }
}
