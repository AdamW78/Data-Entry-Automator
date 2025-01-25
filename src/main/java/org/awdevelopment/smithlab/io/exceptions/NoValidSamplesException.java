package org.awdevelopment.smithlab.io.exceptions;

import java.io.File;

public class NoValidSamplesException extends InputFileException {
    public NoValidSamplesException(File inputFile) {
        super("Error: No valid samples found in the input file \""
                + inputFile.getPath() + "\".");
    }
}
