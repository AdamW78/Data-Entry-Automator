package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.awdevelopment.smithlab.gui.controllers.ConditionsController;
import org.awdevelopment.smithlab.gui.controllers.StrainsController;
import org.awdevelopment.smithlab.gui.controllers.TimepointsController;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.FieldType;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.ValidatableField;

public class EmptyInputSheetFields extends AbstractFields {

    private final MainApplicationController controller;

    private final ValidatableField numTimepointsTextField;
    private final ValidatableField numConditionsTextField;
    private final ValidatableField numStrainsTextField;
    private final ValidatableField numReplicatesTextField;
    private final ValidatableField outputFilenameTextField;
    private final ValidatableField sampleLabelingRadioButtons;
    private final ValidatableField sampleSortingMethodChoiceBox;

    EmptyInputSheetFields(MainApplicationController controller) {
        super(controller, new ValidatableField[] { new ValidatableField(controller.numTimepointsTextField, controller.timepointsAddedLabel, FieldType.BYTE),
                new ValidatableField(controller.numConditionsTextField, controller.numConditionsErrorLabel, FieldType.BYTE),
                new ValidatableField(controller.numStrainsTextField, controller.numStrainErrorLabel, FieldType.BYTE),
                new ValidatableField(controller.numReplicatesEmptyInputSheetTextField, controller.numReplicatesErrorLabelEmptyInputSheet, FieldType.BYTE),
                new ValidatableField(controller.outputFilenameEmptyInputSheetTextField, controller.statusLabelEmptyInputSheet, FieldType.FILENAME),
                new ValidatableField(controller.sampleSortingMethodEmptyInputSheetChoiceBox, controller.statusLabelEmptyInputSheet, FieldType.CHOICE_BOX),
                new ValidatableField(controller.sampleLabelingRadioButtons, controller.statusLabelEmptyInputSheet, FieldType.RADIO_BUTTONS) });
        this.numTimepointsTextField = super.getValidatableFields()[0];
        this.numConditionsTextField = super.getValidatableFields()[1];
        this.numStrainsTextField = super.getValidatableFields()[2];
        this.numReplicatesTextField = super.getValidatableFields()[3];
        this.outputFilenameTextField = super.getValidatableFields()[4];
        this.sampleSortingMethodChoiceBox = super.getValidatableFields()[5];
        this.sampleLabelingRadioButtons = super.getValidatableFields()[6];
        this.controller = controller;

    }

    public ValidatableField getNumTimepointsTextField() { return numTimepointsTextField; }
    public ValidatableField getNumConditionsTextField() { return numConditionsTextField; }
    public ValidatableField getNumStrainsTextField() { return numStrainsTextField; }
    public ValidatableField getNumReplicatesTextField() { return numReplicatesTextField; }
    public ValidatableField getOutputFilenameTextField() { return outputFilenameTextField; }
    public ValidatableField getSampleSortingMethodChoiceBox() { return sampleSortingMethodChoiceBox; }
    public ValidatableField getSampleLabelingRadioButtons() { return sampleLabelingRadioButtons; }
    public Label getNumConditionsErrorLabel() { return controller.numConditionsErrorLabel; }
    public Label getNumTimepointsErrorLabel() { return controller.numTimepointsErrorLabel; }
    public Label getNumStrainErrorLabel() { return controller.numStrainErrorLabel; }
    public Label getNumReplicatesErrorLabel() { return controller.numReplicatesErrorLabelEmptyInputSheet; }
    public HBox getStrainsHBox() { return controller.strainsHBox; }
    public HBox getConditionsHBox() { return controller.conditionsHBox; }
    public RadioButton getConditionLabelingRadioButton() { return controller.conditionLabelingRadioButton; }
    public RadioButton getStrainLabelingRadioButton() { return controller.strainLabelingRadioButton; }
    public RadioButton getConditionAndStrainLabelingRadioButton() { return controller.conditionAndStrainLabelingRadioButton; }
    public CheckBox getIncludeBaselineColumnCheckbox() { return controller.includeBaselineColumnCheckbox; }
    public Label getStatusLabel() { return controller.statusLabelEmptyInputSheet; }
    public Label getTimepointsAddedLabel() { return controller.timepointsAddedLabel; }
    public TimepointsController getTimepointsController() { return controller.timepointsController; }
    public StrainsController getStrainsController() { return controller.strainsController; }
    public ConditionsController getConditionsController() { return controller.conditionsController; }
    public RadioButton getSelectedRadioButton() {
        String[] radioButtonIDs = new String[] { "conditionLabelingRadioButton", "strainLabelingRadioButton", "conditionAndStrainLabelingRadioButton" };
        for (String radioButtonID : radioButtonIDs)
            if (((RadioButton) controller.getControlByID(radioButtonID)).isSelected()) return (RadioButton) controller.getControlByID(radioButtonID);
        return null;
    }
}
