package org.awdevelopment.smithlab.io.output;

public class XlsxEmptyInputSheetWriter {

    private final String emptyInputSheetName;
    private final boolean verbose;
    private final short numReplicates;

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, boolean verbose, short numReplicates) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.verbose = verbose;
        this.numReplicates = numReplicates;
    }

    public void writeEmptyInputSheet() {
        if (verbose) System.out.println("Writing empty input sheet: " + emptyInputSheetName);

    }
}
