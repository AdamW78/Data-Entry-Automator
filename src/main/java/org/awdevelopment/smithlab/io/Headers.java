package org.awdevelopment.smithlab.io;

import org.awdevelopment.smithlab.data.Day;

import java.util.HashMap;

public class Headers {
    private boolean conditionColumn, strainColumn, sampleNumberColumn;
    private final HashMap<Day, Integer> days;
    private final HashMap<String, Integer> headers;

    public Headers() {
        this.conditionColumn = false;
        this.strainColumn = false;
        this.sampleNumberColumn = false;
        days = new HashMap<>();
        headers = new HashMap<>();
    }

    public void addHeader(String header, int index) {
        headers.put(header, index);
        updateColumn(header);
    }

    public void addDay(short dayNumber, int index) {
        days.put(new Day(dayNumber), index);
    }

    private void updateColumn(String header) {
        switch (header.toLowerCase()) {
            case "condition":
                conditionColumn = true;
                break;
            case "strain":
                strainColumn = true;
                break;
            case "sample number":
                sampleNumberColumn = true;
                break;
        }
    }

    public boolean checkHeaders() {
        return (conditionColumn || strainColumn) && sampleNumberColumn;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "conditionColumn=" + conditionColumn +
                ", strainColumn=" + strainColumn +
                ", sampleNumberColumn=" + sampleNumberColumn +
                ", days=" + days +
                '}';
    }
}
