package org.awdevelopment.smithlab.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Strain {

    private Condition condition;
    private final String name;
    private final Set<Sample> samples;

    public Strain(String name) {
        this.name = name;
        samples = new HashSet<>();
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
    public Optional<Condition> getCondition() { return Optional.ofNullable(condition); }
    public void setCondition(Condition condition) { this.condition = condition; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Strain strain = (Strain) obj;
        return name.equals(strain.name) && condition.equals(strain.condition);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + condition.hashCode();
    }
}
