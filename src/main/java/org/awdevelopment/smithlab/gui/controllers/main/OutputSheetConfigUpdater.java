package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.awdevelopment.smithlab.config.ConfigOption;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.io.output.formats.OutputType;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;

public class OutputSheetConfigUpdater extends AbstractConfigUpdater {

    MainApplicationController controller;
    private final OutputSheetsConfig config;
    private final OutputSheetFields fields;
    private final GUILogger guiLogger;
    private final LoggerHelper LOGGER;

    OutputSheetConfigUpdater(MainApplicationController controller, OutputSheetsConfig config) {
        super(config, controller.outputSheetValidator, controller.outputSheetFields);
        this.controller = controller;
        this.config = config;
        this.fields = controller.outputSheetFields;
        this.guiLogger = controller.guiLogger;
        this.LOGGER = config.LOGGER();
    }

    public void browseForInputFile() {
        LOGGER.atDebug("Browsing for input file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input File (ending in .xlsx)");
        File inputFile = fileChooser.showOpenDialog(fields.getInputFileBrowseButton().getScene().getWindow());
        if (inputFile != null) {
            LOGGER.atDebug("Selected input file with path: \"" + inputFile.getAbsolutePath() + "\"");
            getTextField(fields.getInputFileTextField()).setText(inputFile.getAbsolutePath());
            fields.getInputFileTextField().setStatus(FieldStatus.EDITED_NOT_VALIDATED);
            updateInputFile(null);
        }
    }

    public void handleRadioButtonPressOutputSheets(ActionEvent actionEvent) {
        updateFields();
        RadioButton radioButton = (RadioButton) actionEvent.getSource();
        RadioButton[] radioButtons = new RadioButton[] { fields.getOutputStylePrismRadioButton(), fields.getOutputStyleTestsRadioButton(), fields.getOutputStyleRawRadioButton(), fields.getOutputStyleBothRadioButton() };
        for (RadioButton otherRadioButton : radioButtons) if (!otherRadioButton.equals(radioButton)) otherRadioButton.setSelected(false);
        OutputType oldOutputType = config.outputType();
        if (fields.getOutputStylePrismRadioButton().isSelected()) config.setOutputType(OutputType.PRISM);
        else if (fields.getOutputStyleTestsRadioButton().isSelected()) config.setOutputType(OutputType.STATISTICAL_TESTS);
        else if (fields.getOutputStyleRawRadioButton().isSelected()) config.setOutputType(OutputType.RAW);
        else if (fields.getOutputStyleBothRadioButton().isSelected()) config.setOutputType(OutputType.BOTH);
        LOGGER.atDebug("Radio button press ActionEvent caught: Switched from OutputType \""
                + oldOutputType +"\" to new OutputType \"" + radioButton.getText() + "\".");
    }

    public void handleAddSheetsCheckbox() {
        updateFields();
        if (fields.getAddSheetsToInputFileCheckbox().isSelected()) {
            getTextField(fields.getOutputFileTextField()).setDisable(true);
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(getTextField(fields.getInputFileTextField()).getText());
            if (!fields.getOutputFilenameErrorLabel().getText().isEmpty())
                guiLogger.clearError(fields.getOutputFilenameErrorLabel());
            LOGGER.atDebug("Add sheets to input file checkbox selected - output file text field disabled.");
        } else {
            getTextField(fields.getOutputFileTextField()).setDisable(false);
            config.setWriteToDifferentFile(true);
            config.setOutputFilename(getTextField(fields.getOutputFileTextField()).getText());
            LOGGER.atDebug("Add sheets to input file checkbox deselected - output file text field enabled.");
        }
    }

    public void updateNumReplicates(KeyEvent keyEvent) { updateTextField(fields.getNumReplicatesTextField(), ConfigOption.NUMBER_OF_REPLICATES, keyEvent); }

    public void updateOutputFilename(KeyEvent keyEvent) { updateTextField(fields.getOutputFileTextField(), ConfigOption.OUTPUT_FILE, keyEvent); }

    public void updateInputFile(KeyEvent keyEvent) { updateTextField(fields.getInputFileTextField(), ConfigOption.INPUT_FILE, keyEvent); }

    @SuppressWarnings("unchecked")
    public void updateSampleSortingMethod() {
        try {
            config.set(ConfigOption.SORT_OPTION, ((ChoiceBox<SortOption>) fields.getControlByIDAndMode(fields.getSampleSortingMethodChoiceBox().getControlID(), config.mode())).getValue());
        } catch (IllegalFieldAccessException e) {
            LOGGER.atFatal("Error occurred while updating sample sorting method choice box.", e);
            LOGGER.atFatal(e.getMessage());
            System.exit(1);
        }
    }

    public void updateFields() {
        updateNumReplicates(null);
        updateOutputFilename(null);
        updateInputFile(null);
        updateSampleSortingMethod();
    }
}
