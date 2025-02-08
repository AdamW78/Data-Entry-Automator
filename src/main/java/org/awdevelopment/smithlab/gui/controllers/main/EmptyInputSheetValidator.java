package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;

public class EmptyInputSheetValidator extends AbstractValidator {

    private final EmptyInputSheetFields fields;
    private final GUILogger guiLogger;
    private final EmptyInputSheetConfig config;
    private boolean failedEmptyNumTimepointsEmptyInputSheet = false;
    private boolean failedEmptyNumReplicatesEmptyInputSheet = false;
    private boolean failedEmptyOutputFilenameEmptyInputSheet = false;
    private boolean failedEmptyConditionsEmptyInputSheet = false;
    private boolean failedEmptyStrainsEmptyInputSheet = false;

    public EmptyInputSheetValidator(EmptyInputSheetFields fields, GUILogger guiLogger, EmptyInputSheetConfig config) {
        super(fields, guiLogger, config);
        this.fields = fields;
        this.guiLogger = guiLogger;
        this.config = config;
    }

    @Override
    public boolean preliminaryFieldsValid() {
        return false;
    }

    @Override
    public boolean fieldsValid() {
        return false;
    }

    private boolean validateNumTimepoints(boolean preventEmpty) {
        if (fields.getTimepointsController() == null || fields.getTimepointsController().usingNumDays()) {
            return validateTextFieldByte(fields.getNumTimepointsTextField(), fields.getNumTimepointsErrorLabel(), preventEmpty);
        } else {
            guiLogger.clearError(fields.getNumTimepointsErrorLabel());
            config.setUsingNumDays(fields.getTimepointsController().usingNumDays());
            config.setDays(fields.getTimepointsController().getDays());
            return true;
        }
    }

    private boolean validateOutputFilename(boolean preventEmpty) {
        return validateTextFieldFilename(fields.getOutputFilenameTextField(), fields.getStatusLabel(), preventEmpty);
    }

    private boolean checkOutputFilename(TextField textField, boolean failedEmptyBoolean, Label errorLabel) {
        if (outputFilenameEmptyInputSheetsTextField.getText().isEmpty() && !failedEmptyOutputFilenameEmptyInputSheet) {
            guiLogger.clearError(statusLabelEmptyInputSheets);
            return true;
        } else if (outputFilenameEmptyInputSheetsTextField.getText().isEmpty()){
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter an output filename");
            return false;
        } else if (!outputFilenameEmptyInputSheetsTextField.getText().endsWith(".xlsx")) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Output filename must end in .xlsx");
            return false;
        } else {
            guiLogger.clearError(statusLabelEmptyInputSheets);
            return true;
        }
    }

    private boolean checkNumReplicatesEmptyInputSheet() {
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty() && !failedEmptyNumReplicatesEmptyInputSheet) {
            guiLogger.clearError(statusLabelEmptyInputSheets);
            return true;
        }
        else if (numReplicatesEmptyInputSheetTextField.getText().isEmpty()) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of replicates");
            return false;
        }
        try {
            long numReplicates = Long.parseLong(numReplicatesEmptyInputSheetTextField.getText());
            if (numReplicates < 1){
                guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Number must be > 0");
                return false;
            }
            else if (numReplicates > 127) {
                guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Number must be <= 127");
                return false;
            }
            else {
                guiLogger.clearError(statusLabelEmptyInputSheets);
                return true;
            }
        } catch (NumberFormatException e) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Invalid number: \"" + numReplicatesEmptyInputSheetTextField.getText() + "\"");
            return false;
        }
    }

    private boolean checkNumConditionsEmptyInputSheet() {
        if (conditionsController == null || conditionsController.usingNumConditions()) {
            if (numConditionsTextField.getText().isEmpty() && !failedEmptyConditionsEmptyInputSheet) {
                guiLogger.clearError(numConditionsErrorLabel);
                return true;
            } else if (numConditionsTextField.getText().isEmpty()) {
                guiLogger.errorOccurred(numConditionsErrorLabel, "Error: Please enter a number of conditions");
                return false;
            } else {
                try {
                    long numConditions = Long.parseLong(numConditionsTextField.getText());
                    if (numConditions < 1) {
                        guiLogger.errorOccurred(numConditionsErrorLabel, "Error: Number of conditions must be > 0");
                        return false;
                    } else if (numConditions > 127) {
                        guiLogger.errorOccurred(numConditionsErrorLabel, "Error: Number of conditions must be <= 127");
                        return false;
                    } else {
                        guiLogger.clearError(numConditionsErrorLabel);
                        config.setNumConditions((byte) numConditions);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    guiLogger.errorOccurred(numConditionsErrorLabel, "Error: Invalid number: \"" + numConditionsTextField.getText() + "\"");
                    return false;
                }
            }
        } else {
            guiLogger.clearError(numConditionsErrorLabel);
            config.setUsingNumConditions(conditionsController.usingNumConditions());
            config.setConditions(conditionsController.getConditions());
            return true;
        }
    }
    private boolean checkNumStrainsEmptyInputSheet() {
        if (strainsController == null || strainsController.usingNumStrains()) {
            if (numStrainsTextField.getText().isEmpty() && !failedEmptyStrainsEmptyInputSheet) {
                guiLogger.clearError(numStrainErrorLabel);
                return true;
            } else if (numStrainsTextField.getText().isEmpty()) {
                guiLogger.errorOccurred(numStrainErrorLabel, "Error: Please enter a number of strains");
                return false;
            } else {
                try {
                    long numStrains = Long.parseLong(numStrainsTextField.getText());
                    if (numStrains < 1) {
                        guiLogger.errorOccurred(numStrainErrorLabel, "Error: Number of strains must be > 0");
                        return false;
                    } else if (numStrains > 127) {
                        guiLogger.errorOccurred(numStrainErrorLabel, "Error: Number of strains must be <= 127");
                        return false;
                    } else {
                        guiLogger.clearError(numStrainErrorLabel);
                        config.setNumStrains((byte) numStrains);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    guiLogger.errorOccurred(numStrainErrorLabel, "Error: Invalid number: \"" + numStrainsTextField.getText() + "\"");
                    return false;
                }
            }
        } else {
            guiLogger.clearError(numStrainErrorLabel);
            config.setUsingNumStrains(strainsController.usingNumStrains());
            config.setStrains(strainsController.getStrains());
            return true;
        }
    }

    private boolean checkSampleLabelingRadioButtons() {
        if (!conditionLabelingRadioButton.isSelected() && !strainLabelingRadioButton.isSelected() && !conditionAndStrainLabelingRadioButton.isSelected()) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please select a sample labeling method");
            return false;
        }
        return true;
    }

    @Override
    boolean validateTextFieldNotEmpty(TextField textField, Label statusLabel, boolean preventEmpty) {
        boolean failedBoolean = switch (textField.getId()) {
            case "numReplicatesEmptyInputSheetTextField" -> failedEmptyNumReplicatesEmptyInputSheet;
            case "outputFilenameEmptyInputSheetsTextField" -> failedEmptyOutputFilenameEmptyInputSheet;
            case "numConditionsTextField" -> failedEmptyConditionsEmptyInputSheet;
            case "numStrainsTextField" -> failedEmptyStrainsEmptyInputSheet;
            case "numTimepointsTextField" -> failedEmptyNumTimepointsEmptyInputSheet;
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
}
