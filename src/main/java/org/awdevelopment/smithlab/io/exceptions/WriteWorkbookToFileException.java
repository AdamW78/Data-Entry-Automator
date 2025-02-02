package org.awdevelopment.smithlab.io.exceptions;

public class WriteWorkbookToFileException extends OutputException {
    public WriteWorkbookToFileException(String filename, Exception e) {
        super("Error writing workbook to file: " + filename + " - " + e.getMessage());
    }

    @Override
    public String getDisplayName() {
        return "Failed Writing Workbook to File";
    }

}
