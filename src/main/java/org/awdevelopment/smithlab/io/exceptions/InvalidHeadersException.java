package org.awdevelopment.smithlab.io.exceptions;

public class InvalidHeadersException extends InputFileException {
    public InvalidHeadersException() {
        super("Error: Invalid headers in input file. Make sure the input file has at least one column with the header 'Condition' or 'Strain' and one column with the header 'Sample Number' (all case-insensitive).");
    }

    protected InvalidHeadersException(String message) {
        super(message);
    }

    @Override
    public String getExceptionDisplayName() {
        return "Header were Invalid";
    }
}
