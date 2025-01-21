package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.output.formats.*;

import java.io.File;
import java.io.IOException;

public class OutputGenerator {

    private final OutputStyle outputStyle;
    private final String outputFileName;
    private final boolean writeToDifferentFile;
    private final File inputFile;

    public OutputGenerator(Config config) {
        switch (config.outputType()) {
            case PRISM:
                outputStyle = new PrismOutputStyle(config.sortOption());
                break;
            case OTHER:
                outputStyle = new OtherOutputStyle();
                break;
            case RAW:
                outputStyle = new RawOutputStyle();
                break;
            case BOTH:
                outputStyle = new BothOutputStyle(config.sortOption());
                break;
            default:
                throw new IllegalArgumentException("Invalid output type: " + config.outputType());
        }
        if (config.writeToDifferentFile()) this.outputFileName = config.outputFile();
        else this.outputFileName = config.inputFile().getName();
        this.writeToDifferentFile = config.writeToDifferentFile();
        this.inputFile = config.inputFile();
    }
    public void generateOutput(Experiment experiment) {
        XlsxOutputWriter writer = new XlsxOutputWriter(outputStyle, writeToDifferentFile, inputFile);
        try {
            writer.writeOutput(outputFileName, experiment);
        } catch (OutputException e) {
            System.out.println(e.getMessage());
        }
    }
}
