package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.AbstractConfig;

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

    abstract boolean validateTextFieldNotEmpty(TextField textField, Label statusLabel, boolean preventEmpty);

    protected boolean validateTextFieldFilename(TextField textField, Label statusLabel, boolean preventEmpty) {
        if (!validateTextFieldNotEmpty(textField, statusLabel, preventEmpty)) return false;
        if (!textField.getText().endsWith(".xlsx")) {
            guiLogger.errorOccurred(statusLabel, "Error: Filename must end in .xlsx");
            return false;
        } else {
            guiLogger.clearError(statusLabel);
            return true;
        }
    }

    protected boolean validateTextFieldByte(TextField textField, Label statusLabel, boolean preventEmpty) {
        if (!validateTextFieldNotEmpty(textField, statusLabel, preventEmpty)) return false;
        try {
            long longValue = Long.parseLong(textField.getText());
            if (longValue <= 0) {
                guiLogger.errorOccurred(statusLabel, "Error: Must be a value > 0");
                return false;
            } else if (longValue > Byte.MAX_VALUE) {
                guiLogger.errorOccurred(statusLabel, "Error: Must be a value <= " + Byte.MAX_VALUE);
                return false;
            } else {
                guiLogger.clearError(statusLabel);
                return true;
            }
        } catch (NumberFormatException e) {
            guiLogger.errorOccurred(statusLabel, "Error: Please enter a valid number");
            return false;
        }
    }

    GUILogger guiLogger() { return guiLogger; }

    AbstractFields fields() { return fields; }

    AbstractConfig config() { return config; }
}
