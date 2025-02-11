package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.AbstractConfig;

import java.io.File;

public abstract class AbstractValidator {

    private final AbstractFields fields;
    private final GUILogger guiLogger;
    private final AbstractConfig config;

    protected AbstractValidator(AbstractFields fields, GUILogger guiLogger, AbstractConfig config) {
        this.fields = fields;
        this.guiLogger = guiLogger;
        this.config = config;
    }

    public abstract boolean preliminaryFieldsValid();

    public abstract boolean fieldsValid();

    abstract boolean validateTextFieldNotEmpty(ValidatableField textField, boolean preventEmpty);

    protected boolean validateTextFieldFileExists(ValidatableField field, boolean preventEmpty) {
        if (!validateTextFieldFilename(field, preventEmpty)) return false;
        TextField textField = getTextField(field);
        String filePath = textField.getText();
        File file = new File(filePath);
        if (!file.exists()) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: File does not exist");
            return false;
        } else {
            guiLogger.clearError(field.getErrorLabel());
            return true;
        }
    }

    protected boolean validateTextFieldFilename(ValidatableField field, boolean preventEmpty) {
        if (!validateTextFieldNotEmpty(field, preventEmpty)) return false;
        TextField textField = getTextField(field);
        if (!textField.getText().endsWith(".xlsx")) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: Filename must end in .xlsx");
            return false;
        } else {
            guiLogger.clearError(field.getErrorLabel());
            return true;
        }
    }

    protected boolean validateTextFieldByte(ValidatableField field, boolean preventEmpty) {
        if (!validateTextFieldNotEmpty(field, preventEmpty)) return false;
        TextField textField = getTextField(field);
        try {
            long longValue = Long.parseLong(textField.getText());
            if (longValue <= 0) {
                guiLogger.errorOccurred(field.getErrorLabel(), "Error: Must be a value > 0");
                return false;
            } else if (longValue > Byte.MAX_VALUE) {
                guiLogger.errorOccurred(field.getErrorLabel(), "Error: Must be a value <= " + Byte.MAX_VALUE);
                return false;
            } else {
                guiLogger.clearError(field.getErrorLabel());
                return true;
            }
        } catch (NumberFormatException e) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: Please enter a valid number");
            return false;
        }
    }

    TextField getTextField(ValidatableField field) { return (TextField) field.getControl(); }

    GUILogger guiLogger() { return guiLogger; }

    AbstractFields fields() { return fields; }

    AbstractConfig config() { return config; }
}
