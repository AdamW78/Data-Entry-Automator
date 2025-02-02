package org.awdevelopment.smithlab.io.exceptions;

public class FailedToCreateNewWorkbookException extends OutputException {
    public FailedToCreateNewWorkbookException() {
        super("Error: Failed to create a new workbook.");
    }

    @Override
    public String getDisplayName() {
        return "Failed to Create a New Workbook";
    }
}
