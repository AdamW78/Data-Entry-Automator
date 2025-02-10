package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.data.Strain;

import java.util.HashSet;
import java.util.Set;

public class StrainsController extends AbstractLabelController {

    private boolean usingNumStrains;
    private final Set<Strain> strains;

    public StrainsController() {
        super();
        strains = new HashSet<>();
    }

    @Override
    public void setup() {}

    public boolean usingNumStrains() { return usingNumStrains; }

    public void setUsingNumStrains(boolean usingNumStrains) { this.usingNumStrains = usingNumStrains; }
    public Set<Strain> getStrains() { return strains; }
}
