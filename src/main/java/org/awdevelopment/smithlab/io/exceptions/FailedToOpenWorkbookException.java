package org.awdevelopment.smithlab.io.exceptions;

import java.io.File;

public class FailedToOpenWorkbookException extends InputFileException {
    public FailedToOpenWorkbookException(File xlsxFile, String message) {
        super("Error: Failed to open Excel workbook from file \"" + xlsxFile.getPath()
                + "\"\n This was caused by: " + message);
    }
}
