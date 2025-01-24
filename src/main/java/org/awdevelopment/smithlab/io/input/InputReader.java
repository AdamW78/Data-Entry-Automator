package org.awdevelopment.smithlab.io.input;

import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.io.exceptions.InputFileWarningException;

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
        } catch (InputFileException | InputFileWarningException e) {
            throw new RuntimeException(e);
        }
    }
}
