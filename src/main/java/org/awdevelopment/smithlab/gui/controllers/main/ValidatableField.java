package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class ValidatableField {

    private final Control control;
    private final Label errorLabel;
    private final Control[] controls;
    private boolean beenTouched = false;
    private boolean failedEmptyCheck = false;
    private final FieldType fieldType;

    public ValidatableField(Control control, Label errorLabel, FieldType fieldType) {
        this.control = control;
        controls = null;
        this.errorLabel = errorLabel;
        this.fieldType = fieldType;
    }

    public ValidatableField(Control[] controls, Label errorLabel, FieldType fieldType) {
        this.controls = controls;
        control = null;
        this.errorLabel = errorLabel;
        this.fieldType = fieldType;
    }

    public void reset() { resetTouch(); resetFailedEmptyCheck(); }

    public void touch() { beenTouched = true; }

    public void resetTouch() { beenTouched = false; }

    public void failedEmptyCheck() { failedEmptyCheck = true; }

    public void resetFailedEmptyCheck() { failedEmptyCheck = false; }

    public boolean beenTouched() { return beenTouched; }

    public boolean hasFailedEmptyCheck() { return failedEmptyCheck; }

    public Control getControl() { return control; }

    public Control[] getControls() { return controls; }

    public Label getErrorLabel() { return errorLabel; }

    public FieldType getFieldType() { return fieldType; }
}
