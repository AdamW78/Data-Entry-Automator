package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;

public class EmptyInputSheetValidator extends AbstractValidator {

    private final EmptyInputSheetFields fields;
    private final GUILogger guiLogger;
    private final EmptyInputSheetConfig config;
    private boolean failedEmptyNumTimepoints = false;
    private boolean failedEmptyNumReplicates = false;
    private boolean failedEmptyOutputFilename = false;
    private boolean failedEmptyConditions = false;
    private boolean failedEmptyStrains = false;

    public EmptyInputSheetValidator(EmptyInputSheetFields fields, GUILogger guiLogger, EmptyInputSheetConfig config) {
        super(fields, guiLogger, config);
        this.fields = fields;
        this.guiLogger = guiLogger;
        this.config = config;
    }

    @Override
    public boolean preliminaryFieldsValid() { return fieldsValid(false); }
    @Override
    public boolean fieldsValid() { return fieldsValid(true); }

    boolean fieldsValid(boolean preventEmpty) {
        return validateNumTimepoints(preventEmpty)
                && validateNumReplicates(preventEmpty)
                && validateOutputFilename(preventEmpty)
                && validateNumConditions(preventEmpty)
                && validateNumStrains(preventEmpty)
                && validateSampleLabelingRadioButtons();
    }

    protected boolean fieldsValidExceptNumTimepoints() {
        return validateNumReplicates(false)
                && validateOutputFilename(false)
                && validateNumConditions(false)
                && validateNumStrains(false)
                && validateSampleLabelingRadioButtons();
    }

    protected boolean fieldsValidExceptNumReplicates() {
        return validateNumTimepoints(false)
                && validateOutputFilename(false)
                && validateNumConditions(false)
                && validateNumStrains(false)
                && validateSampleLabelingRadioButtons();
    }

    protected boolean fieldsValidExceptOutputFilename() {
        return validateNumTimepoints(false)
                && validateNumReplicates(false)
                && validateNumConditions(false)
                && validateNumStrains(false)
                && validateSampleLabelingRadioButtons();
    }

    protected boolean fieldsValidExceptNumConditions() {
        return validateNumTimepoints(false)
                && validateNumReplicates(false)
                && validateOutputFilename(false)
                && validateNumStrains(false)
                && validateSampleLabelingRadioButtons();
    }

    protected boolean fieldsValidExceptNumStrains() {
        return validateNumTimepoints(false)
                && validateNumReplicates(false)
                && validateOutputFilename(false)
                && validateNumConditions(false)
                && validateSampleLabelingRadioButtons();
    }

    boolean validateNumTimepoints(boolean preventEmpty) {
        if (fields.getTimepointsController() == null || fields.getTimepointsController().usingNumDays()) {
            if (validateTextFieldByte(fields.getNumTimepointsTextField(), fields.getNumTimepointsErrorLabel(), preventEmpty)) {
                failedEmptyNumTimepoints = false;
                return true;
            } else {
                if (preventEmpty && !validateTextFieldNotEmpty(fields.getNumTimepointsTextField(), fields.getNumTimepointsErrorLabel(), true)) {
                    failedEmptyNumTimepoints = true;
                }
                return false;
            }
        } else {
            guiLogger.clearError(fields.getNumTimepointsErrorLabel());
            config.setUsingNumDays(fields.getTimepointsController().usingNumDays());
            config.setDays(fields.getTimepointsController().getDays());
            return true;
        }
    }

    boolean validateOutputFilename(boolean preventEmpty) {
        if (validateTextFieldFilename(fields.getOutputFilenameTextField(), fields.getStatusLabel(), preventEmpty)) {
            failedEmptyOutputFilename = false;
            return true;
        } else {
            if (preventEmpty && !validateTextFieldNotEmpty(fields.getOutputFilenameTextField(), fields.getStatusLabel(), true)) {
                failedEmptyOutputFilename = true;
            }
            return false;
        }
    }

    boolean validateNumReplicates(boolean preventEmpty) {
        if (validateTextFieldByte(fields.getNumReplicatesTextField(), fields.getStatusLabel(), preventEmpty)) {
            failedEmptyNumReplicates = false;
            return true;
        } else {
            if (preventEmpty && !validateTextFieldNotEmpty(fields.getNumReplicatesTextField(), fields.getStatusLabel(), true)) {
                failedEmptyNumReplicates = true;
            }
            return false;
        }
    }

    boolean validateNumConditions(boolean preventEmpty) {
        if (fields.getConditionsController() == null || fields.getConditionsController().usingNumConditions()) {
            if (validateTextFieldByte(fields.getNumConditionsTextField(), fields.getNumConditionsErrorLabel(), preventEmpty)) {
                failedEmptyConditions = false;
                return true;
            } else {
                if (preventEmpty && !validateTextFieldNotEmpty(fields.getNumConditionsTextField(), fields.getNumConditionsErrorLabel(), true)) {
                    failedEmptyConditions = true;
                }
                return false;
            }
        } else {
            guiLogger.clearError(fields.getNumConditionsErrorLabel());
            config.setUsingNumConditions(fields.getConditionsController().usingNumConditions());
            config.setConditions(fields.getConditionsController().getConditions());
            return true;
        }
    }
    boolean validateNumStrains(boolean preventEmpty) {
        if (fields.getStrainsController() == null || fields.getStrainsController().usingNumStrains()) {
            if (validateTextFieldByte(fields.getNumStrainsTextField(), fields.getNumStrainErrorLabel(), preventEmpty)) {
                failedEmptyStrains = false;
                return true;
            } else {
                if (preventEmpty && !validateTextFieldNotEmpty(fields.getNumStrainsTextField(), fields.getNumStrainErrorLabel(), true)) {
                    failedEmptyStrains = true;
                }
                return false;
            }
        } else {
            guiLogger.clearError(fields.getNumStrainErrorLabel());
            config.setUsingNumStrains(fields.getStrainsController().usingNumStrains());
            config.setStrains(fields.getStrainsController().getStrains());
            return true;
        }
    }

    boolean validateSampleLabelingRadioButtons() {
        if (!fields.getStrainLabelingRadioButton().isSelected()
                && !fields.getConditionLabelingRadioButton().isSelected()
                && !fields.getConditionAndStrainLabelingRadioButton().isSelected()) {
            guiLogger.errorOccurred(fields.getStatusLabel(), "Error: Please select a sample labeling option");
            return false;
        } else {
            guiLogger.clearError(fields.getStatusLabel());
            return true;
        }
    }

    @Override
    boolean validateTextFieldNotEmpty(TextField textField, Label statusLabel, boolean preventEmpty) {
        boolean failedBoolean = switch (textField.getId()) {
            case "numReplicatesEmptyInputSheetTextField" -> failedEmptyNumReplicates;
            case "outputFilenameEmptyInputSheetsTextField" -> failedEmptyOutputFilename;
            case "numConditionsTextField" -> failedEmptyConditions;
            case "numStrainsTextField" -> failedEmptyStrains;
            case "numTimepointsTextField" -> failedEmptyNumTimepoints;
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

    boolean failedEmptyNumTimepoints() { return failedEmptyNumTimepoints; }

    boolean failedEmptyNumReplicates() { return failedEmptyNumReplicates; }

    boolean failedEmptyOutputFilename() { return failedEmptyOutputFilename; }

    boolean failedEmptyConditions() { return failedEmptyConditions; }

    boolean failedEmptyStrains() { return failedEmptyStrains; }
}
