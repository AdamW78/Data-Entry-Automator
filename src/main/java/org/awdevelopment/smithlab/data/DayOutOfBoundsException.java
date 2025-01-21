package org.awdevelopment.smithlab.data;

public class DayOutOfBoundsException extends RuntimeException {
    public DayOutOfBoundsException(short day) {
        super("Day "+ day + " is out of bounds. ");
    }
}
