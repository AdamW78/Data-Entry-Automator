package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.output.formats.*;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;

public class OutputGenerator {

    private final boolean GUI;
    private final OutputStyle outputStyle;
    private final String outputFileName;
    private final boolean writeToDifferentFile;
    private final File inputFile;
    private final LoggerHelper LOGGER;
    private final String emptyInputSheetName;
    private final short numReplicates;

    public OutputGenerator(Config config, LoggerHelper logger) {
        LOGGER = logger;
        switch (config.outputType()) {
            case PRISM:
                outputStyle = new PrismOutputStyle(config.sortOption());
                break;
            case STATISTICAL_TESTS:
                outputStyle = new StatisticalTestsOutputStyle(config.sortOption(), config.numberOfReplicates());
                break;
            case RAW:
                outputStyle = new RawOutputStyle(config.sortOption());
                break;
            case BOTH:
                outputStyle = new BothOutputStyle(config.sortOption(), config.numberOfReplicates());
                break;
            default:
                // This is redundant because the compiler thinks that outputStyle is not initialized
                outputStyle = null;
                LOGGER.atError("Output type not recognized");
                System.exit(0);
        }
        if (config.writeToDifferentFile()) this.outputFileName = config.outputFilename();
        else this.outputFileName = config.inputFile().getPath();
        this.writeToDifferentFile = config.writeToDifferentFile();
        this.inputFile = config.inputFile();
        this.emptyInputSheetName = config.emptyInputSheetName();
        this.numReplicates = config.numberOfReplicates();
        this.GUI = config.GUI();
    }
    public void generateOutput(Experiment experiment) throws OutputException {
        XlsxOutputWriter writer = new XlsxOutputWriter(outputStyle, writeToDifferentFile, inputFile);
        try {
            writer.writeOutput(outputFileName, experiment);
        } catch (OutputException e) {
            if (GUI) {
                LOGGER.atError("Error writing output: " + e.getMessage());
                LOGGER.atError("Passing exception to GUI to display error message...");
                throw e;
            } else {
                LOGGER.atError("Error writing output: " + e.getMessage() + " -  Exiting...");
                System.out.println("Error writing output: " + e.getMessage() + " - Exiting...");
                System.exit(0);
            }
        }

    }

    public void generateEmptyInputSheet() {
        XlsxEmptyInputSheetWriter writer = new XlsxEmptyInputSheetWriter(emptyInputSheetName, numReplicates, LOGGER);
        writer.writeEmptyInputSheet();
    }
}
