package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.config.SortOption;

public class OutputSheetValidator extends AbstractValidator {

    private final OutputSheetFields fields;
    private final GUILogger guiLogger;
    private final OutputSheetsConfig config;

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
    boolean validateTextFieldNotEmpty(ValidatableField field, Label statusLabel, boolean preventEmpty) {
        TextField textField = (TextField) field.getControl();
        if (!field.beenTouched() && !preventEmpty) return true;
        else if (textField.getText().isEmpty() && (preventEmpty || field.hasFailedEmptyCheck())) {
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
            config.setNumberOfReplicates(Byte.parseByte(((TextField) fields.getNumReplicatesTextField().getControl()).getText()));
            return true;
        } else return false;
    }

    @SuppressWarnings("unchecked")
    boolean sampleSortingValid() {
        return ((ChoiceBox<SortOption>) fields.getSampleSortingMethodChoiceBox().getControl()).getValue() != null;
    }

    boolean outputTypeValid() {
        return fields.getOutputStylePrismRadioButton().isSelected()
                ^ fields.getOutputStyleTestsRadioButton().isSelected()
                ^ fields.getOutputStyleRawRadioButton().isSelected()
                ^ fields.getOutputStyleBothRadioButton().isSelected();
    }
}
