package org.awdevelopment.smithlab.io.exceptions;

public class InvalidDayNumberException extends InvalidHeadersException {
    public InvalidDayNumberException(Exception e, double dayNumber) {
        super("Error while reading day number: \"" + dayNumber + "\" - " + e.getMessage());
    }
}
