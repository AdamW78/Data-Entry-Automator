package org.awdevelopment.smithlab.io.exceptions;

public class FailedToCreateNewWorkbookException extends OutputException {
    public FailedToCreateNewWorkbookException() {
        super("Failed to create a new workbook with the Apache POI library.");
    }

    @Override
    public String getDisplayName() {
        return "Failed to Create a New Workbook";
    }
}
