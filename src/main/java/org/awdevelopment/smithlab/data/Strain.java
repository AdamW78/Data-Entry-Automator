package org.awdevelopment.smithlab.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Strain {

    private Optional<Condition> conditionOptional;
    private String name;
    private Set<Sample> samples;

    public Strain(String name) {
        this.name = name;
        samples = new HashSet<>();
        conditionOptional = Optional.empty();
    }

    public String getName() {
        return name;
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public void addSample(Sample sample) {
        samples.add(sample);
    }

    @Override
    public String toString() {
        return name;
    }

    public Optional<Condition> getCondition() {
        return conditionOptional;
    }

    public void addCondition(Condition condition) {
        conditionOptional = Optional.of(condition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Strain strain = (Strain) obj;
        return name.equals(strain.name) && conditionOptional.equals(strain.conditionOptional);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + conditionOptional.hashCode();
    }
}
