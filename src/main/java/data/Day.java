package data;

public class Day {
    private final short day;

    public Day(short day) {
        if (day < 0 || day > 50) {
            throw new IllegalArgumentException("Day must be between 0 and 50");
        }
        this.day = day;
    }

    public short getDay() {
        return day;
    }
}
