package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.logging.LoggerHelper;

public class XlsxEmptyInputSheetWriter {

    private final String emptyInputSheetName;
    private final short numReplicates;
    private final LoggerHelper LOGGER;

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, short numReplicates, LoggerHelper logger) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.numReplicates = numReplicates;
        this.LOGGER = logger;
    }

    public void writeEmptyInputSheet() {
        LOGGER.atInfo("Writing empty input sheet \"" + emptyInputSheetName + "\"...");
        // Write empty input sheet

    }
}
