package io;

import data.Day;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Headers {
    private boolean conditionColumn, strainColumn, sampleNumberColumn, baselineColumn;
    private HashSet<Day> days;
    private final HashMap<String, Integer> headers;

    public Headers() {
        this.conditionColumn = false;
        this.strainColumn = false;
        this.sampleNumberColumn = false;
        this.baselineColumn = false;
        days = new HashSet<>();
        headers = new HashMap<>();
    }

    public void addHeader(String header, int index) {
        if (header.toLowerCase().contains("day ")) {
            short dayNumber = Short.parseShort(header.substring(4));
            addDay(dayNumber);
        }
        else {
            headers.put(header, index);
            updateColumn(header);
        }
    }

    public void addDay(short dayNumber) {
        days.add(new Day(dayNumber));
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
            case "baseline":
                baselineColumn = true;
                break;
        }
    }

    public boolean hasConditionColumn() {
        return conditionColumn;
    }

    public void setConditionColumn(boolean conditionColumn) {
        this.conditionColumn = conditionColumn;
    }

    public boolean hasStrainColumn() {
        return strainColumn;
    }

    public void setStrainColumn(boolean strainColumn) {
        this.strainColumn = strainColumn;
    }

    public boolean hasSampleNumberColumn() {
        return sampleNumberColumn;
    }

    public void setSampleNumberColumn(boolean sampleNumberColumn) {
        this.sampleNumberColumn = sampleNumberColumn;
    }

    public boolean hasBaselineColumn() {
        return baselineColumn;
    }

    public void setBaselineColumn(boolean baselineColumn) {
        this.baselineColumn = baselineColumn;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }
}
