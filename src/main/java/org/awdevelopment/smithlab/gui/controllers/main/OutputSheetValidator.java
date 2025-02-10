package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;

public class OutputSheetValidator extends AbstractValidator {

    private final OutputSheetFields fields;
    private final GUILogger guiLogger;
    private final OutputSheetsConfig config;
    private boolean failedEmptyReplicates = false;
    private boolean failedEmptyOutputFilename = false;
    private boolean failedEmptyInputFile = false;

    protected OutputSheetValidator(OutputSheetFields fields, GUILogger guiLogger, OutputSheetsConfig config) {
        super(fields, guiLogger, config);
        this.fields = fields;
        this.guiLogger = guiLogger;
        this.config = config;
    }

    @Override
    public boolean preliminaryFieldsValid() { return fieldsValid(false); }

    @Override
    public boolean fieldsValid() { return fieldsValid(true); }

    private boolean fieldsValid(boolean preventEmpty) {
        return numReplicatesTextFieldValid(preventEmpty)
             && outputFilenameTextFieldValid(preventEmpty)
             && inputFileTextFieldValid(preventEmpty)
             && sampleSortingValid()
             && outputTypeValid();
    }

    @Override
    boolean validateTextFieldNotEmpty(TextField textField, Label statusLabel, boolean preventEmpty) {
        boolean failedBoolean = switch (textField.getId()) {
            case "numReplicatesTextField" -> failedEmptyReplicates;
            case "outputFilenameTextField" -> failedEmptyOutputFilename;
            case "outputFilenameEmptyInputSheetsTextField" -> failedEmptyInputFile;
            default -> false;
        };
        if (textField.getText().isEmpty() && (preventEmpty || failedBoolean)) {
            guiLogger.errorOccurred(statusLabel, "Error: Please enter a value");
            return false;
        } else {
            guiLogger.clearError(statusLabel);
            return true;
        }
    }

    boolean inputFileTextFieldValid(boolean preventEmpty) {
        return validateTextFieldFileExists(fields.getInputFileTextField(),
                fields.getInputFileExistsLabel(), preventEmpty);
    }

    boolean outputFilenameTextFieldValid(boolean preventEmpty) {
        if (fields.getAddSheetsToInputFileCheckbox().isSelected()) return true;
        else return validateTextFieldFilename(fields.getOutputFileTextField(),
                fields.getOutputFilenameErrorLabel(), preventEmpty);
    }

    boolean numReplicatesTextFieldValid(boolean preventEmpty) {
        if (validateTextFieldByte(fields.getNumReplicatesTextField(),
                fields.getReplicatesErrorLabelOutputSheet(), preventEmpty)) {
            config.setNumberOfReplicates(Byte.parseByte(fields.getNumReplicatesTextField().getText()));
            return true;
        } else return false;
    }

    boolean sampleSortingValid() {
        return fields.getSampleSortingMethodChoiceBox().getValue() != null;
    }

    boolean outputTypeValid() {
        return fields.getOutputStylePrismRadioButton().isSelected()
                ^ fields.getOutputStyleTestsRadioButton().isSelected()
                ^ fields.getOutputStyleRawRadioButton().isSelected()
                ^ fields.getOutputStyleBothRadioButton().isSelected();
    }

    boolean failedEmptyReplicates() { return failedEmptyReplicates; }
    boolean failedEmptyOutputFilename() { return failedEmptyOutputFilename; }
    boolean failedEmptyInputFile() { return failedEmptyInputFile; }

}
