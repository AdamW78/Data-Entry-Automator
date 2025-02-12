package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.AbstractConfig;
import org.awdevelopment.smithlab.config.ConfigOption;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public abstract class AbstractConfigUpdater {

    private final AbstractConfig config;
    private final AbstractValidator validator;
    LoggerHelper LOGGER() { return LOGGER; }
    private final LoggerHelper LOGGER;

    AbstractConfigUpdater(AbstractConfig config, AbstractValidator validator) {
        this.config = config;
        this.validator = validator;
        this.LOGGER = config.LOGGER();
    }

    public abstract void updateFields();

    public abstract void updateSampleSortingMethod();

    void updateTextField(ValidatableField field, ConfigOption option, KeyEvent keyEvent) {
        // If doing normal typing in properly marked field, exit early
        if (field.status() == FieldStatus.EDITED_NOT_VALIDATED && (field.getControl().isFocused()
                && !(((TextField) field.getControl()).getText().isEmpty()) && keyEvent.getCode() != KeyCode.ENTER
                && keyEvent.getCode() != KeyCode.TAB))
            return;
        LOGGER.atDebug("");
        LOGGER.atDebug("Begin process of updating stored value for field with id: \"" + field.getControl().getId() + "\"...");
        TextField textField = (TextField) field.getControl();
        boolean fieldIsFocused = textField.isFocused();
        if (fieldIsFocused) LOGGER.atDebug("Field is focused.");
        else LOGGER.atDebug("Field is not focused.");
        switch (field.status()) {
            case EDITED_NOT_VALIDATED -> {
                LOGGER.atDebug("Field status: Edited but not validated.");
                if (!fieldIsFocused) {
                    LOGGER.atDebug("Validating...");
                    validator.validateTextFieldByID(field.getControl().getId());
                    updateIfValidated(field, option);
                } else if (textField.getText().isEmpty()) {
                    LOGGER.atDebug("Field is empty, setting field status to empty.");
                    field.setStatus(FieldStatus.EMPTY);
                }
            }
            case EMPTY -> {
                LOGGER.atDebug("Field status: Empty.");
                if (fieldIsFocused) {
                    if (keyEvent != null && !keyEvent.getText().isEmpty()) {
                        LOGGER.atDebug("Key event is not null and is non-empty, setting field status to edited but not validated.");
                        field.setStatus(FieldStatus.EDITED_NOT_VALIDATED);
                    } else {
                        LOGGER.atDebug("Key event is null or empty, field status remains empty.");
                        field.setStatus(FieldStatus.EMPTY);
                    }
                } else {
                    LOGGER.atDebug("Field is empty. Running validation to produce error message (field status will be set to invalid).");
                    validator.validateTextFieldByID(field.getControl().getId());
                }
            }
            case INVALID -> {
                LOGGER.atDebug("Field status: Invalid.");
                if (!fieldIsFocused) {
                    LOGGER.atDebug("Field will remain invalid.");
                } else if (keyEvent != null && !keyEvent.getText().isEmpty()) {
                    LOGGER.atDebug("Key event is not null and is non-empty, validating field...");
                    validator.validateTextFieldByID(field.getControl().getId());
                    updateIfValidated(field, option);
                }
            }
            case READY -> {
                LOGGER.atDebug("Field status: Ready.");
                if (fieldIsFocused) {
                    if (keyEvent != null && !keyEvent.getText().isEmpty()) {
                        LOGGER.atDebug("Key event is not null and is non-empty, setting field status to edited but not validated.");
                        field.setStatus(FieldStatus.EDITED_NOT_VALIDATED);
                    } else {
                        LOGGER.atDebug("Key event is null or empty, field status remains ready.");
                        field.setStatus(FieldStatus.READY);
                    }
                } else LOGGER.atDebug("Field is not focused, skipping validation as field is ready...");
            }
            case UNTOUCHED -> {
                LOGGER.atDebug("Field status: Untouched.");
                if (fieldIsFocused) {
                    if (keyEvent != null && !keyEvent.getText().isEmpty()) {
                        LOGGER.atDebug("Key event is not null and is non-empty, setting field status to edited but not validated.");
                        field.setStatus(FieldStatus.EDITED_NOT_VALIDATED);
                    } else {
                        LOGGER.atDebug("Key event is null or empty, field status remains untouched.");
                        field.setStatus(FieldStatus.UNTOUCHED);
                    }
                } else LOGGER.atDebug("Field is not focused, skipping validation as field is untouched...");
            }
        }
    }

    void updateIfValidated(ValidatableField field, ConfigOption option) {
        LOGGER.atDebug("Checking if field was validated successfully...");
        if (field.status() == FieldStatus.READY) {
            LOGGER.atDebug("Field was validated successfully, updating stored value...");
            switch (field.getFieldType()) {
                case BYTE -> config.set(option, Byte.parseByte(getTextField(field).getText()));
                case FILENAME, STRING, EXISTING_FILE -> config.set(option, getTextField(field).getText());
                default -> throw new IllegalStateException("Unexpected value: " + field.getFieldType());
            }
            LOGGER.atDebug("Stored value updated successfully.");
        } else {
            LOGGER.atDebug("Field was not validated successfully, not updating stored value.");
        }
    }

    TextField getTextField(ValidatableField field) { return (TextField) field.getControl(); }
}
