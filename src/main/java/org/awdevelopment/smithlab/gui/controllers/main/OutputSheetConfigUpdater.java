package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    private final OutputSheetValidator validator;
    private final GUILogger guiLogger;
    private final LoggerHelper LOGGER;

    OutputSheetConfigUpdater(MainApplicationController controller, OutputSheetsConfig config) {
        this.controller = controller;
        this.config = config;
        this.fields = controller.outputSheetFields;
        this.validator = controller.outputSheetValidator;
        this.guiLogger = controller.guiLogger;
        this.LOGGER = config.LOGGER();
    }

    public void browseForInputFile() {
        LOGGER.atDebug("Browsing for input file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input File (ending in .xlsx)");
        File inputFile = fileChooser.showOpenDialog(fields.getInputFileBrowseButton().getScene().getWindow());
        if (inputFile != null) {
            LOGGER.atDebug("Selected input file: " + inputFile.getAbsolutePath());
            ((TextField) fields.getInputFileTextField().getControl()).setText(inputFile.getAbsolutePath());
            updateInputFile(null);
        }
    }

    public void handleRadioButtonPressOutputSheets(ActionEvent actionEvent) {
        updateFields();
        RadioButton radioButton = (RadioButton) actionEvent.getSource();
        for (RadioButton otherRadioButton : ((RadioButton[]) fields.getRadioButtons().getControls())) if (!otherRadioButton.equals(radioButton)) otherRadioButton.setSelected(false);
        OutputType oldOutputType = config.outputType();
        if (fields.getOutputStylePrismRadioButton().isSelected()) config.setOutputType(OutputType.PRISM);
        else if (fields.getOutputStyleTestsRadioButton().isSelected()) config.setOutputType(OutputType.STATISTICAL_TESTS);
        else if (fields.getOutputStyleRawRadioButton().isSelected()) config.setOutputType(OutputType.RAW);
        else if (fields.getOutputStyleBothRadioButton().isSelected()) config.setOutputType(OutputType.BOTH);
        LOGGER.atTrace("Radio button press ActionEvent caught: Switched from OutputType \""
                + oldOutputType +"\" to new OutputType \"" + radioButton.getText() + "\".");
    }

    public void handleAddSheetsCheckbox() {
        updateFields();
        if (fields.getAddSheetsToInputFileCheckbox().isSelected()) {
            fields.getOutputFileTextField().getControl().setDisable(true);
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(((TextField) fields.getInputFileTextField().getControl()).getText());
            if (!fields.getOutputFilenameErrorLabel().getText().isEmpty())
                guiLogger.clearError(fields.getOutputFilenameErrorLabel());
        } else {
            fields.getOutputFileTextField().getControl().setDisable(false);
            config.setWriteToDifferentFile(true);
            config.setOutputFilename(((TextField) fields.getOutputFileTextField().getControl()).getText());
        }
    }

    void updateTextField(TextField textField, ConfigOption option, KeyEvent keyEvent, Label errorLabel, boolean failedBoolean, boolean byteParse, boolean fileExists) {
        if (!byteParse && !fileExists) config.set(option, textField.getText());
        if (textField.getText().isEmpty() && !failedBoolean) guiLogger.clearError(errorLabel);
        if (keyEvent == null
                || !(textField.getScene().focusOwnerProperty().get().getId().equals(textField.getId()))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            boolean validatedSuccessfully = switch (textField.getId()) {
                case "numReplicatesTextField" -> validator.numReplicatesTextFieldValid(failedBoolean);
                case "outputFileTextField" -> validator.outputFilenameTextFieldValid(failedBoolean);
                case "inputFileTextField" -> validator.inputFileTextFieldValid(failedBoolean);
                default -> throw new IllegalStateException("Unexpected value: " + textField.getId());
            };
            if (byteParse && validatedSuccessfully) config.set(option, Byte.parseByte(textField.getText()));
            if (fileExists && validatedSuccessfully) config.set(option, textField.getText());
        }
    }

    public void updateNumReplicates(KeyEvent keyEvent) {
        updateTextField(((TextField) fields.getNumReplicatesTextField().getControl()), ConfigOption.NUMBER_OF_REPLICATES, keyEvent,
                fields.getReplicatesErrorLabelOutputSheet(), validator.failedEmptyReplicates(),
                true, false);
    }


    public void updateOutputFilename(KeyEvent keyEvent) {
        updateTextField(((TextField) fields.getOutputFileTextField().getControl()), ConfigOption.OUTPUT_FILE, keyEvent,
                fields.getOutputFilenameErrorLabel(), validator.failedEmptyOutputFilename(),
                false, false);
    }

    public void updateInputFile(KeyEvent keyEvent) {
        updateTextField(((TextField) fields.getInputFileTextField().getControl()), ConfigOption.INPUT_FILE, keyEvent,
                fields.getInputFileExistsLabel(), validator.failedEmptyInputFile(),
                false, true);
    }


    @SuppressWarnings("unchecked")
    public void updateSampleSortingMethod() {
        config.set(ConfigOption.SORT_OPTION, ((ChoiceBox<SortOption>) fields.getSampleSortingMethodChoiceBox().getControl()).getValue());
    }

    public void updateFields() {
        updateNumReplicates(null);
        updateOutputFilename(null);
        updateInputFile(null);
        updateSampleSortingMethod();
    }
}
