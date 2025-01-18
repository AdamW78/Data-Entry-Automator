package org.awdevelopment.smithlab.data;

public record Day(short day) {
    public Day {
        if (day < 0 || day > 50) {
            throw new IllegalArgumentException("Day must be between 0 and 50");
        }
    }

    @Override
    public String toString() {
        return "[Day " + day + "]";
    }
}
