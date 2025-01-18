package org.awdevelopment.smithlab.data;

import java.util.HashSet;
import java.util.Set;

public class Experiment {
    private Set<Condition> conditions;
    private Set<Strain> strains;
    private Set<Sample> samples;

    public Experiment() {
        conditions = new HashSet<>();
        strains = new HashSet<>();
        samples = new HashSet<>();
    }

    public void addSample(Sample sample) {
        if (sample.getCondition().isPresent()) {
            conditions.add(sample.getCondition().get());
        }
        if (sample.getStrain().isPresent()) {
            strains.add(sample.getStrain().get());
        }
        samples.add(sample);
    }

    public void addConditions(Set<Condition> conditions) {
        this.conditions.addAll(conditions);
    }

    public void addStrains(Set<Strain> strains) {
        this.strains.addAll(strains);
    }
}
