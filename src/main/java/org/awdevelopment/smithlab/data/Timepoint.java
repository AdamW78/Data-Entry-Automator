package org.awdevelopment.smithlab.data;

import org.apache.poi.xssf.usermodel.XSSFCell;

public record Timepoint(short dayNumber, int colonies, Dilution dilution, XSSFCell originalCell, XSSFCell dilutionCell) {

    private int getFactor(Dilution dilution) {
        return switch (dilution) {
            case Dilution.x10 -> 10;
            case Dilution.x100 -> 100;
            case Dilution.x1000 -> 1000;
        };
    }

    public int getActualValue() {
        return colonies * getFactor(dilution);
    }

    @Override
    public String toString() {
        return "[Day: " + dayNumber + ", Colonies: " + colonies + ", Dilution: " + dilution + ", Cell: " + originalCell.getAddress() + "]";
    }
}
