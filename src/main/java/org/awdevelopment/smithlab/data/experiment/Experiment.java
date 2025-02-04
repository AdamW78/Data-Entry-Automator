package org.awdevelopment.smithlab.data.experiment;

import org.awdevelopment.smithlab.data.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Experiment extends AbstractExperiment {

    private final Set<Sample> samples;
    private Sample[] sortedSamplesAlphabetically;
    private Sample[] sortedSamplesReverseAlphabetically;
    private Sample[] sortedSamplesBySampleNumber;
    private short[] sortedDayNumbers;
    private short numReplicates;

    public Experiment() {
        super();
        samples = new HashSet<>();
    }

    public void addSample(Sample sample) {
        if (sample.hasCondition()) conditions().add(sample.getCondition());
        if (sample.hasStrain()) strains().add(sample.getStrain());
        samples.add(sample);
    }

    public void addConditions(Set<Condition> conditions) { conditions().addAll(conditions);}
    public void addStrains(Set<Strain> strains) { strains().addAll(strains); }
    public Set<Sample> getSamples() {
        return samples;
    }

    public Sample getSampleNumber(int sampleNumber) {
        if (sortedSamplesBySampleNumber == null) {
            sortedSamplesBySampleNumber = samples.stream()
                    .sorted(Comparator.comparing(Sample::getNumber))
                    .toArray(Sample[]::new);
        }
        return sortedSamplesBySampleNumber[sampleNumber];
    }

    public Sample getSampleByAlphabeticalOrder(int sampleNumber) {
        if (sortedSamplesAlphabetically == null) {
            sortedSamplesAlphabetically = samples.stream()
                    .sorted(Comparator.comparing(Sample::getOutputName))
                    .toArray(Sample[]::new);
        }
        return sortedSamplesAlphabetically[sampleNumber];
    }

    public Sample getSampleByReverseAlphabeticalOrder(int sampleNumber) {
        if (sortedSamplesReverseAlphabetically == null) {
            sortedSamplesReverseAlphabetically = samples.stream()
                    .sorted(Comparator.comparing(Sample::getOutputName).reversed())
                    .toArray(Sample[]::new);
        }
        return sortedSamplesReverseAlphabetically[sampleNumber];
    }

    public short[] getDayNumbers() {
        if (sortedDayNumbers == null) {
            Set<Short> days = new HashSet<>();
            for (Sample sample : samples) {
                for (Timepoint timepoint : sample.getTimepoints()) {
                    days.add(timepoint.dayNumber());
                }
            }
            Short[] daysSorted = days.stream().sorted().toArray(Short[]::new);
            sortedDayNumbers = new short[daysSorted.length];
            for (int i = 0; i < daysSorted.length; i++) sortedDayNumbers[i] = daysSorted[i];
        }
        return sortedDayNumbers;
    }

    public short getNumReplicates() {
        return numReplicates;
    }

    public void setNumReplicates(short numReplicates) {
        this.numReplicates = numReplicates;
    }
}
