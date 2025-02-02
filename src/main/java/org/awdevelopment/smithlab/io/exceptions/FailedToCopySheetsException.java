package org.awdevelopment.smithlab.io.exceptions;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class FailedToCopySheetsException extends OutputException {
    public FailedToCopySheetsException(File input, IOException e) {
        super("Error: Failed to copy sheets from " + input.getName() + " to the new workbook.", e);
    }

    public FailedToCopySheetsException(File input, InvalidFormatException e) {
        super("Error: Failed to copy sheets from " + input.getName() + " to the new workbook.", e);
    }

    @Override
    public String getDisplayName() {
        return "Failed to Copy Sheets";
    }
}
