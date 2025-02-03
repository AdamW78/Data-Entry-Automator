package org.awdevelopment.smithlab.io.exceptions;

public class WriteWorkbookToFileException extends OutputException {
    public WriteWorkbookToFileException(String filename, Exception e) {
        super("Error writing workbook to file: \n\"" + filename + "\"\n" + e.getMessage() + "\nIs the file open in another program?");
    }

    @Override
    public String getDisplayName() {
        return "Failed Writing Workbook to File";
    }

}
