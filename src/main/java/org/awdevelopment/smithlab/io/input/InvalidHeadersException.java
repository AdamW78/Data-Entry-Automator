package org.awdevelopment.smithlab.io.input;

public class InvalidHeadersException extends RuntimeException {
    public InvalidHeadersException() {
        super("Error: Invalid headers in input file. Make sure the input file has at least one column with the header 'Condition' or 'Strain' and one column with the header 'Sample Number' (all case-insensitive).");
    }
}
