package org.awdevelopment.smithlab.data;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Experiment {

    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final Set<Sample> samples;

    public Experiment() {
        conditions = new HashSet<>();
        strains = new HashSet<>();
        samples = new HashSet<>();
    }

    public void addSample(Sample sample) {
        if (sample.hasCondition()) conditions.add(sample.getCondition());
        if (sample.hasStrain()) strains.add(sample.getStrain());
        samples.add(sample);
    }

    public void addConditions(Set<Condition> conditions) {
        this.conditions.addAll(conditions);
    }

    public void addStrains(Set<Strain> strains) {
        this.strains.addAll(strains);
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public Set<Strain> getStrains() {
        return strains;
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public Sample getSampleNumber(int sampleNumber) {
        Optional<Sample> sampleOptional = samples.stream().filter(s -> s.getNumber() == sampleNumber).findAny();
        if (sampleOptional.isPresent()) {
            return sampleOptional.get();
        } else {
            throw new SampleNumberOutOfBoundsException(sampleNumber, samples.size());
        }
    }

    public Sample getSampleByAlphabeticalOrder(int sampleNumber) {
        return samples.stream()
                .sorted(Comparator.comparing(Sample::getOutputName))
                .toArray(Sample[]::new)[sampleNumber];
    }

    public Sample getSampleByReverseAlphabeticalOrder(int sampleNumber) {
        return samples.stream()
                .sorted(Comparator.comparing(Sample::getOutputName).reversed())
                .toArray(Sample[]::new)[sampleNumber];
    }

    public short[] getDayNumbers() {
        Set<Short> days = new HashSet<>();
        for (Sample sample : samples) {
            for (Timepoint timepoint : sample.getTimepoints()) {
                days.add(timepoint.dayNumber());
            }
        }
        Short[] sortedDays = days.stream().sorted().toArray(Short[]::new);
        short[] daysArray = new short[sortedDays.length];
        for (int i = 0; i < sortedDays.length; i++) {
            daysArray[i] = sortedDays[i];
        }
        return daysArray;
    }
}
