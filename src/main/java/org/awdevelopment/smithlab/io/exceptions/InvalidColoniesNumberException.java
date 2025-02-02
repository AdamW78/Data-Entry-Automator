package org.awdevelopment.smithlab.io.exceptions;

public class InvalidColoniesNumberException extends InvalidTimepointException {
    public InvalidColoniesNumberException(String value, int row, int column) {
        super("Failed to read cell with value \"" + value + "\" as a colonies number at row " + row + " and column " + column);
    }

    @Override
    public String getDisplayName() {
        return "Invalid Number of Colonies";
    }
}
