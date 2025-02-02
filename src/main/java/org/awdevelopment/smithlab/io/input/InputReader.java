package org.awdevelopment.smithlab.io.input;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class InputReader {

    private final Config config;
    private final LoggerHelper LOGGER;


    public InputReader(Config config, LoggerHelper logger) {
        this.config = config;
        this.LOGGER = logger;
    }

    public Experiment readExperimentData() throws InputFileException {
        XlsxInputReader reader = new XlsxInputReader(config.inputFile(), LOGGER);
        try {
            return reader.readExperimentData();
        } catch (InputFileException e) {
            if (config.GUI()) {
                LOGGER.atError("Error reading input file \""+config.inputFile()+": " + e.getMessage());
                throw e;
            }
            LOGGER.atError("Error reading input file \""+config.inputFile()+": " + e.getMessage() + " Exiting...");
            System.exit(0);
        }
        return null;
    }
}
