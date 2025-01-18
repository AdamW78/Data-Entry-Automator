import data.Experiment;
import formats.OutputType;
import io.input.XlsxInputReader;
import io.output.OutputGenerator;

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
            return;
        } else if (mode == Mode.GENERATE_OUTPUT_SHEETS) {
            XlsxInputReader reader = new XlsxInputReader(inputFile);
            Experiment experiment = reader.readExperimentData();
            OutputGenerator outputGenerator = new OutputGenerator(outputType, writeToDifferentFile, outputFileName, inputFile);
            outputGenerator.generateOutput();
        }
    }

}
