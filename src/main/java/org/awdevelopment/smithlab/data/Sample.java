package org.awdevelopment.smithlab.data;

import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.*;

public class Sample {
    private final int number;
    private final int baseline;
    private final XSSFCell baselineCell;
    private Strain strain;
    private Condition condition;
    private final Set<Timepoint> timepoints;

    public Sample(int number, int baseline, XSSFCell baselineCell, Strain strain, Condition condition, Set<Timepoint> timepoints) {
        this.number = number;
        this.baseline = baseline;
        this.baselineCell = baselineCell;
        if (strain != null) {
            this.strain = strain;
        }
        if (condition != null) {
            this.condition = condition;
        }
        this.timepoints = timepoints;
    }

    public Sample(int number, int baseline, XSSFCell baselineCell, Strain strain, Set<Timepoint> timepoints) {
        this(number, baseline, baselineCell, strain, null, timepoints);
    }

    public Sample(int number, int baseline, XSSFCell baselineCell, Condition condition, Set<Timepoint> timepoints) {
        this(number, baseline, baselineCell, null, condition, timepoints);
    }

    public Strain getStrain() {
        return strain;
    }

    public Condition getCondition() {
        return condition;
    }

    public Set<Timepoint> getTimepoints() {
        return timepoints;
    }

    public int getNumber() {
        return number;
    }

    public int getBaseline() {
        return baseline;
    }

    public boolean hasStrain() {
        return strain != null;
    }

    public boolean hasCondition() {
        return condition != null;
    }

    public boolean hasTimepoints() {
        return !timepoints.isEmpty();
    }

    public Timepoint getTimepointByDay(short day) {
        Optional<Timepoint> timepointOptional = timepoints.stream().filter(t -> t.dayNumber() == day).findAny();
        if (timepointOptional.isPresent()) {
            return timepointOptional.get();
        } else {
            throw new DayOutOfBoundsException(day);
        }
    }

    public String getOutputName() {
        if (hasStrain() && hasCondition()) {
            return strain.getName() + " + " + condition.getName();
        } else if (hasStrain()) {
            return strain.getName();
        } else if (hasCondition()) {
            return condition.getName();
        } else {
            return "Sample " + number;
        }
    }

    @Override
    public String toString() {
        return "Sample{" +
                "number=" + number +
                ", baseline=" + baseline +
                ", strain=" + strain +
                ", condition=" + condition +
                ", timepoints=" + timepoints +
                '}';
    }

    public XSSFCell getBaselineCell() {
        return baselineCell;
    }
}
