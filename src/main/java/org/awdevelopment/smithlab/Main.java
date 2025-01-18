package org.awdevelopment.smithlab;

import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.formats.OutputType;
import org.awdevelopment.smithlab.io.input.XlsxInputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);
        File inputFile = arguments.getInputFile();
        Mode mode = arguments.getMode();
        boolean writeToDifferentFile = arguments.writeToDifferentFile();
        String outputFileName = arguments.getOutputFileName();
        OutputType outputType = arguments.getOutputType();
        if (mode == Mode.GENERATE_EMPTY_INPUT_SHEET) {
        } else if (mode == Mode.GENERATE_OUTPUT_SHEETS) {
            XlsxInputReader reader = new XlsxInputReader(inputFile);
            Experiment experiment = reader.readExperimentData();
            OutputGenerator outputGenerator = new OutputGenerator(outputType, writeToDifferentFile, outputFileName, inputFile);
            outputGenerator.generateOutput(experiment);
        }
    }

}
