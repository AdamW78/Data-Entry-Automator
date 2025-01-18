package org.awdevelopment.smithlab.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Condition {

    private final String name;
    private Optional<Set<Strain>> strains;
    private Set<Sample> samples;

    public Condition(String name) {
        this.name = name;
        strains = Optional.empty();
        samples = new HashSet<>();
    }

    public void addStrain(Strain strain) {
        if (strains.isEmpty()) {
            strains = Optional.of(Set.of(strain));
        } else {
            strains.get().add(strain);
        }
    }

    public void addSample(Sample sample) {
        samples.add(sample);
    }

    public String getName() {
        return name;
    }
    public Optional<Set<Strain>> getStrains() {
        return strains;
    }
    public Set<Sample> getSamples() {
        return samples;
    }

    @Override
    public String toString() {
        return name;
    }
}
