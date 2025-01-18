package org.awdevelopment.smithlab.data;

import java.util.*;

public class Sample {
    private int number;
    private int baseline;
    private Optional<Strain> strain;
    private Optional<Condition> condition;
    private Set<Timepoint> timepoints;

    private Sample(int number, int baseline, Optional<Strain> strain, Optional<Condition> condition, Set<Timepoint> timepoints) {
        this.number = number;
        this.baseline = baseline;
        this.strain = strain;
        this.condition = condition;
        this.timepoints = timepoints;
    }

    public Sample(int number, int baseline, Strain strain, Set<Timepoint> timepoints) {
        this(number, baseline, Optional.of(strain), Optional.empty(), timepoints);
    }

    public Sample(int number, int baseline, Condition condition, Set<Timepoint> timepoints) {
        this(number, baseline, Optional.empty(), Optional.of(condition), timepoints);
    }

    public Sample(int number, int baseline, Strain strain, Condition condition, Set<Timepoint> timepoints) {
        this(number, baseline, Optional.of(strain), Optional.of(condition), timepoints);
    }

    public Optional<Strain> getStrain() {
        return strain;
    }

    public void setStrain(Optional<Strain> strain) {
        this.strain = strain;
    }

    public Optional<Condition> getCondition() {
        return condition;
    }

    public void setCondition(Optional<Condition> condition) {
        this.condition = condition;
    }

    public Set<Timepoint> getTimepoints() {
        return timepoints;
    }

    public void setTimepoints(Set<Timepoint> timepoints) {
        this.timepoints = timepoints;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
