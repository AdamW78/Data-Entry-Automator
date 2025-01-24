package org.awdevelopment.smithlab.io.exceptions;

public class NoStrainOrConditionException extends OutputException {
    public NoStrainOrConditionException() {
        super("Error: While trying to generate the Prism output, there were no strains or conditions to generate the output for.");
    }
}
