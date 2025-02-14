package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.logging.LoggerHelper;

public class ConfigManager {

    public OutputSheetsConfig getOutputSheetsConfig() { return outputSheetsConfig; }

    public EmptyInputSheetConfig getEmptyInputSheetConfig() { return emptyInputSheetConfig; }

    public ImageRecognitionConfig getImageRecognitionConfig() { return imageRecognitionConfig; }

    private final OutputSheetsConfig outputSheetsConfig;
    private final EmptyInputSheetConfig emptyInputSheetConfig;
    private final ImageRecognitionConfig imageRecognitionConfig;

    public ConfigManager(LoggerHelper LOGGER) {
        outputSheetsConfig = new OutputSheetsConfig(LOGGER);
        emptyInputSheetConfig = new EmptyInputSheetConfig(LOGGER);
        imageRecognitionConfig = new ImageRecognitionConfig(LOGGER);
    }

    public <T> void updateConfigValue(Mode mode, ConfigOption configOption, T value) {
        switch (mode) {
            case Mode.GENERATE_OUTPUT_SHEETS -> outputSheetsConfig.set(configOption, value);
            case Mode.GENERATE_EMPTY_INPUT_SHEET -> emptyInputSheetConfig.set(configOption, value);
            case Mode.IMAGE_RECOGNITION -> imageRecognitionConfig.set(configOption, value);
        }
    }

    public <T> T getConfigValue(Mode mode, ConfigOption configOption) {
        return switch (mode) {
            case Mode.GENERATE_OUTPUT_SHEETS -> outputSheetsConfig.get(configOption);
            case Mode.GENERATE_EMPTY_INPUT_SHEET -> emptyInputSheetConfig.get(configOption);
            case Mode.IMAGE_RECOGNITION -> imageRecognitionConfig.get(configOption);
        };
    }

}
