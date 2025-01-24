package org.awdevelopment.smithlab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.gui.FXMLResourceLoader;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;

import static org.awdevelopment.smithlab.gui.FXMLResources.GENERATE_OUTPUT_SHEETS;

public class DataEntryAutomatorApplication extends Application {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length == 0) launch();
        else {
            Arguments arguments = new Arguments(args);
            Config config = new Config(arguments);
            if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) {
                OutputGenerator outputGenerator = new OutputGenerator(config, LOGGER);
                outputGenerator.generateEmptyInputSheet();
            } else if (config.mode() == Mode.GENERATE_OUTPUT_SHEETS) {
                InputReader reader = new InputReader(config, LOGGER);
                Experiment experiment = reader.readExperimentData();
                OutputGenerator outputGenerator = new OutputGenerator(config, LOGGER);
                outputGenerator.generateOutput(experiment);
            }
            System.exit(0);
            }
    }
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = FXMLResourceLoader.load(GENERATE_OUTPUT_SHEETS);
        primaryStage.setTitle("Data Entry Automator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
