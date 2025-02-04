package org.awdevelopment.smithlab.data.experiment;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractExperiment {

    private final Set<Condition> conditions;
    private final Set<Strain> strains;

    protected AbstractExperiment(Set<Condition> conditions, Set<Strain> strains) {
        this.conditions = conditions;
        this.strains = strains;
    }

    protected AbstractExperiment() {
        conditions = new HashSet<>();
        strains = new HashSet<>();
    }

    public Set<Condition> conditions() {
        return conditions;
    }

    public Set<Strain> strains() {
        return strains;
    }

    public boolean hasNoStrainsOrConditions() {
        return strains().isEmpty() && conditions().isEmpty();
    }

    public boolean hasBothStrainsAndConditions() {
        return !strains().isEmpty() && !conditions().isEmpty();
    }

    public boolean hasStrains() {
        return !strains().isEmpty();
    }

    public boolean hasConditions() {
        return !conditions().isEmpty();
    }
}
