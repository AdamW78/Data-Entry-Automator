package org.awdevelopment.smithlab.io.input;

import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;

public class InputReader {

    private final Config config;
    private final Logger LOGGER;


    public InputReader(Config config, Logger logger) {
        this.config = config;
        this.LOGGER = logger;
    }

    public Experiment readExperimentData() {
        XlsxInputReader reader = new XlsxInputReader(config.inputFile(), LOGGER);
        try {
            return reader.readExperimentData();
        } catch (InputFileException e) {
            LOGGER.error(e.getMessage());
            System.exit(0);
        }
        return null;
    }
}
