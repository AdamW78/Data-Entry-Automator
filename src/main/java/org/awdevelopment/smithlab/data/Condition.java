package org.awdevelopment.smithlab.data;

import java.util.LinkedHashSet;
import java.util.Set;

public class Condition {

    private String name;
    private final Set<Strain> strains;
    private final Set<Sample> samples;

    public Condition(String name) {
        this.name = name;
        this.strains = new LinkedHashSet<>();
        this.samples = new LinkedHashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addStrain(Strain strain) {
        strains.add(strain);
    }

    public void addSample(Sample sample) {
        samples.add(sample);
    }

    public String getName() {
        return name;
    }
    public Set<Strain> getStrains() {
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
