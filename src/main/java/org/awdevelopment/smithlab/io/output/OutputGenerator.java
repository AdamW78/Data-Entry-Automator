package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.formats.*;

import java.io.File;

public class OutputGenerator {

    private final OutputStyle outputStyle;
    private final String outputFileName;

    public OutputGenerator(OutputType outputType, boolean writeToDifferentFile, String outputFileName, File inputFile) {
        switch (outputType) {
            case PRISM:
                outputStyle = new PrismOutputStyle();
                break;
            case OTHER:
                outputStyle = new OtherOutputStyle();
                break;
            case RAW:
                outputStyle = new RawOutputStyle();
                break;
            case BOTH:
                outputStyle = new BothOutputStyle();
                break;
            default:
                throw new IllegalArgumentException("Invalid output type: " + outputType);
        }
        if (writeToDifferentFile) this.outputFileName = outputFileName;
        else this.outputFileName = inputFile.getName();
    }

    public void generateOutput(Experiment experiment) {
        XlsxOutputWriter writer = new XlsxOutputWriter(outputStyle);
        try {
            writer.writeOutput(outputFileName, experiment);
        } catch (OutputException e) {
            System.out.println(e.getMessage());
        }

    }
}
