package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.data.Condition;

import java.util.HashSet;
import java.util.Set;

public class ConditionsController extends AbstractLabelController {

    private boolean usingNumConditions;
    private final Set<Condition> conditions;

    public ConditionsController() {
        super();
        conditions = new HashSet<>();
    }

    @Override
    public void setup() {}

    public boolean usingNumConditions() { return usingNumConditions; }

    public void setUsingNumConditions(boolean usingNumConditions) {
        this.usingNumConditions = usingNumConditions;
    }

    public Set<Condition> getConditions() { return conditions; }
}
