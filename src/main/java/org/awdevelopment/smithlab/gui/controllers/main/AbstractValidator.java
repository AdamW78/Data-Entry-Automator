package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.TextField;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.FieldStatus;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.FieldType;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.ValidatableField;
import org.awdevelopment.smithlab.logging.GUILogger;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;

public abstract class AbstractValidator {

    private final Mode mode;
    public LoggerHelper LOGGER() { return LOGGER; }
    private final LoggerHelper LOGGER;
    private final GUILogger guiLogger;
    private final AbstractFields fields;
    private final ValidatableField[] validatableFields;

    protected AbstractValidator(LoggerHelper logger, GUILogger guiLogger, AbstractFields fields, Mode mode) {
        this.guiLogger = guiLogger;
        this.validatableFields = fields.getValidatableFields();
        this.fields = fields;
        this.LOGGER = logger;
        this.mode = mode;
    }

    public void preliminaryFieldsValid() {
        LOGGER.atTrace("Checking if all fields are valid (empty/untouched fields are okay)...");
        for (ValidatableField field : validatableFields) {
            if (field.status() == FieldStatus.EDITED_NOT_VALIDATED) validateTextFieldByID(field.getControlID());
        }
    }

    public boolean fieldsValid() {
        LOGGER.atDebug("Checking if all fields are valid...");
        for (ValidatableField field : validatableFields) {
            if (field.status() == FieldStatus.UNTOUCHED && (field.getFieldType() == FieldType.BYTE
                    || field.getFieldType() == FieldType.STRING || field.getFieldType() == FieldType.FILENAME
                    || field.getFieldType() == FieldType.EXISTING_FILE)) {
                field.setStatus(FieldStatus.EMPTY);
                validateTextFieldByID(field.getControlID());
            } else if (field.status() == FieldStatus.EDITED_NOT_VALIDATED) {
                validateTextFieldByID(field.getControlID());
            }
            if (field.status() == FieldStatus.INVALID || field.status() == FieldStatus.EMPTY) {
                LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is invalid or empty.");
                LOGGER.atDebug("Field status: " + field.status());
                LOGGER.atDebug("Output will not be generated!");
                return false;
            }
        }
        return true;
    }

    abstract void validateTextFieldByID(String id);

    void validateTextFieldNotEmpty(ValidatableField field) {
        if (field.status() == FieldStatus.UNTOUCHED) return;
        TextField textField = getTextField(field);
        if (textField.getText().isEmpty()) {
            field.setStatus(FieldStatus.INVALID);
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: Please enter a value");
            LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is empty.");
        } else {
            guiLogger.clearError(field.getErrorLabel());
            LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is not empty.");
            field.setStatus(FieldStatus.READY);
        }
    }

    void validateTextFieldFileExists(ValidatableField field) {
        if (field.status() == FieldStatus.UNTOUCHED) return;
        validateTextFieldFilename(field);
        TextField textField = getTextField(field);
        String filePath = textField.getText();
        File file = new File(filePath);
        if (!file.exists()) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: File does not exist");
            LOGGER.atDebug("File with path: \"" + filePath + "\" does not exist.");
            field.setStatus(FieldStatus.INVALID);
        } else {
            guiLogger.clearError(field.getErrorLabel());
            LOGGER.atDebug("File with path: \"" + filePath + "\" exists.");
            field.setStatus(FieldStatus.READY);
        }
    }

    void validateTextFieldFilename(ValidatableField field) {
        if (field.status() == FieldStatus.UNTOUCHED) return;
        validateTextFieldNotEmpty(field);
        TextField textField = getTextField(field);
        if (!textField.getText().endsWith(".xlsx")) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: Filename must end in .xlsx");
            LOGGER.atDebug("Filename: \"" + textField.getText() + "\" does not end in .xlsx.");
            field.setStatus(FieldStatus.INVALID);
        } else {
            guiLogger.clearError(field.getErrorLabel());
            LOGGER.atDebug("Filename: \"" + textField.getText() + "\" ends in .xlsx.");
            field.setStatus(FieldStatus.READY);
        }
    }

    void validateTextFieldByte(ValidatableField field) {
        if (field.status() == FieldStatus.UNTOUCHED) return;
        validateTextFieldNotEmpty(field);
        TextField textField = getTextField(field);
        try {
            long longValue = Long.parseLong(textField.getText());
            if (longValue <= 0) {
                guiLogger.errorOccurred(field.getErrorLabel(), "Error: Must be a value > 0");
                LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is <= 0.");
                field.setStatus(FieldStatus.INVALID);
            } else if (longValue > Byte.MAX_VALUE) {
                guiLogger.errorOccurred(field.getErrorLabel(), "Error: Must be a value <= " + Byte.MAX_VALUE);
                LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is > " + Byte.MAX_VALUE + ".");
                field.setStatus(FieldStatus.INVALID);
            } else {
                guiLogger.clearError(field.getErrorLabel());
                LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" has a valid positive byte-value.");
                field.setStatus(FieldStatus.READY);
            }
        } catch (NumberFormatException e) {
            guiLogger.errorOccurred(field.getErrorLabel(), "Error: Please enter a valid number");
            LOGGER.atDebug("Field with id: \"" + field.getControlID() + "\" is not a valid number: \"" + textField.getText() + "\"");
            field.setStatus(FieldStatus.INVALID);
        }
    }

    TextField getTextField(ValidatableField field) {
        try {
            return (TextField) fields.getControlByIDAndMode(field.getControlID(), mode);
        } catch (IllegalFieldAccessException e) {
            LOGGER.atError("Error occurred while validating text field with id: \"" + field.getControlID() + "\"", e);
            System.exit(1);
            return null;
        }
    }

}
