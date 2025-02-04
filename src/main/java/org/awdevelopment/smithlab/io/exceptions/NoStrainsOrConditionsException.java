package org.awdevelopment.smithlab.io.exceptions;

public class NoStrainsOrConditionsException extends OutputException {

    public NoStrainsOrConditionsException() {
        super("Error: While trying to generate the output, there were no strains or conditions to generate the output for.");
    }


    @Override
    public String getDisplayName() {
        return "No Strains or Conditions";
    }
}
