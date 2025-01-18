package org.awdevelopment.smithlab.data;

public class Timepoint {

    private short dayNumber;
    private int colonies;
    private Dilution dilution;

    public Timepoint(short dayNumber, int colonies, Dilution dilution) {
        this.dayNumber = dayNumber;
        this.colonies = colonies;
        this.dilution = dilution;
    }

    private int getFactor(Dilution dilution) {
        return switch (dilution) {
            case Dilution.x10 -> 10;
            case Dilution.x100 -> 100;
            case Dilution.x1000 -> 1000;
        };
    }

    public short getDayNumber() {
        return dayNumber;
    }

    public int getColonies() {
        return colonies;
    }

    public Dilution getDilution() {
        return dilution;
    }

    public int getActualValue() {
        return colonies * getFactor(dilution);
    }

    @Override
    public String toString() {
        return "[Day: " + dayNumber + ", Colonies: " + colonies + ", Dilution: " + dilution + "]";
    }
}
