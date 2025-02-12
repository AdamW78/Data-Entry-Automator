package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

import java.util.Arrays;

public class ValidatableField {

    private final String controlID;
    private final Label errorLabel;
    private final String[] controlIDs;
    private final FieldType fieldType;
    private FieldStatus fieldStatus;

    private ValidatableField(String controlID, String[] controlIDs, Label errorLabel, FieldType fieldType, FieldStatus fieldStatus) {
        this.controlID = controlID;
        this.controlIDs = controlIDs;
        this.errorLabel = errorLabel;
        this.fieldType = fieldType;
        this.fieldStatus = fieldStatus;
    }

    public ValidatableField(Control control, Label errorLabel, FieldType fieldType) {
        this(control.getId(), null, errorLabel, fieldType, FieldStatus.UNTOUCHED);
    }

    public ValidatableField(Control[] controls, Label errorLabel, FieldType fieldType) {
        this(null, Arrays.stream(controls).map(Node::getId).toArray(String[]::new), errorLabel, fieldType, FieldStatus.UNTOUCHED);
    }

    public String getControlID() { return controlID;  }

    public String[] getControlIDs() { return controlIDs;  }

    public Label getErrorLabel() { return errorLabel; }

    public FieldType getFieldType() { return fieldType; }

    public FieldStatus status() { return fieldStatus; }

    public void setStatus(FieldStatus fieldStatus) { this.fieldStatus = fieldStatus; }

}
