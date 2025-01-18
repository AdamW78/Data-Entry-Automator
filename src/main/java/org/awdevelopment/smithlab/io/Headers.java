package org.awdevelopment.smithlab.io;

import org.awdevelopment.smithlab.data.Day;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Headers {
    private boolean conditionColumn, strainColumn, sampleNumberColumn;
    private final HashSet<Day> days;
    private final HashSet<Header> headers;

    public Headers() {
        this.conditionColumn = false;
        this.strainColumn = false;
        this.sampleNumberColumn = false;
        days = new HashSet<>();
        headers = new HashSet<>();
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public Set<Day> getDays() {
        return days;
    }

    public void addHeader(String header, int index) {
        switch (header.toLowerCase()) {
            case "condition":
                conditionColumn = true;
                headers.add(new Header(header, HeaderType.CONDITION, index));
            break;
            case "strain":
                strainColumn = true;
                headers.add(new Header(header, HeaderType.STRAIN, index));
                break;
            case "sample number":
                sampleNumberColumn = true;
                headers.add(new Header(header, HeaderType.SAMPLE_NUMBER, index));
                break;
            case "baseline":
                headers.add(new Header(header, HeaderType.BASELINE, index));
                break;
            default:
                headers.add(new Header(header, HeaderType.UNKNOWN, index));
                break;
        }
    }

    public void addDay(short dayNumber, int index) {
        for (short i = 0; i < 3; i++) {
            days.add(new Day(dayNumber, index + i, i));
        }
    }

    public boolean checkHeaders() {
        return (conditionColumn || strainColumn) && sampleNumberColumn;
    }

    public Optional<Header> getHeader(int index) {
        for (Header header : headers) if (header.index() == index) return Optional.of(header);
        return Optional.empty();
    }

    public Optional<Day> getDay(int index) {
        for (Day day : days) if (day.index() == index) return Optional.of(day);
        return Optional.empty();
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
