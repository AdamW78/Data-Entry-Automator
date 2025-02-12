package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import org.awdevelopment.smithlab.gui.controllers.AbstractLabelController;

public class ValidatableField {

    private final Control control;
    private final Label errorLabel;
    private final Control[] controls;
    private final FieldType fieldType;
    private FieldStatus fieldStatus;

    private ValidatableField(Control control, Control[] controls, Label errorLabel, FieldType fieldType, FieldStatus fieldStatus) {
        this.control = control;
        this.controls = controls;
        this.errorLabel = errorLabel;
        this.fieldType = fieldType;
        this.fieldStatus = fieldStatus;
    }

    public ValidatableField(Control control, Label errorLabel, FieldType fieldType) {
        this(control, null, errorLabel, fieldType, FieldStatus.UNTOUCHED);
    }

    public ValidatableField(Control[] controls, Label errorLabel, FieldType fieldType) {
        this(null, controls, errorLabel, fieldType, FieldStatus.UNTOUCHED);
    }

    public Control getControl() { return control; }

    public Control[] getControls() { return controls; }

    public Label getErrorLabel() { return errorLabel; }

    public FieldType getFieldType() { return fieldType; }

    public FieldStatus status() { return fieldStatus; }

    public void setStatus(FieldStatus fieldStatus) { this.fieldStatus = fieldStatus; }

}
