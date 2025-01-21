package org.awdevelopment.smithlab;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.input.XlsxInputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;

public class Main {

    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);
        Config config = new Config(arguments);
        if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) {
        } else if (config.mode() == Mode.GENERATE_OUTPUT_SHEETS) {
            XlsxInputReader reader = new XlsxInputReader(config);
            Experiment experiment = reader.readExperimentData();
            OutputGenerator outputGenerator = new OutputGenerator(config);
            outputGenerator.generateOutput(experiment);
        }
    }

}
