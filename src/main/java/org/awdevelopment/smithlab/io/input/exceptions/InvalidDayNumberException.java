package org.awdevelopment.smithlab.io.input.exceptions;

public class InvalidDayNumberException extends RuntimeException {
    public InvalidDayNumberException(Exception e, double dayNumber) {
        super("Error while reading day number: \"" + dayNumber + "\" - " + e.getMessage());
    }
}
