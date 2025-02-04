package org.awdevelopment.smithlab.io.exceptions;

public class NoDaysException extends OutputException {

    public NoDaysException() {
        super("Error: No days provided - cannot generate output.");
    }

    @Override
    public String getDisplayName() {
        return "No Days Provided";
    }
}
