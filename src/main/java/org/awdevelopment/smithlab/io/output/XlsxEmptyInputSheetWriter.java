package org.awdevelopment.smithlab.io.output;

import org.apache.logging.log4j.Logger;

public class XlsxEmptyInputSheetWriter {

    private final String emptyInputSheetName;
    private final short numReplicates;
    private final Logger LOGGER;

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, short numReplicates, Logger logger) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.numReplicates = numReplicates;
        this.LOGGER = logger;
    }

    public void writeEmptyInputSheet() {
        String logMessage = "Writing empty input sheet \"" + emptyInputSheetName + "\"...";
        LOGGER.atInfo().log(logMessage);
        // Write empty input sheet

    }
}
