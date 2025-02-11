package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.gui.controllers.ConditionsController;
import org.awdevelopment.smithlab.gui.controllers.StrainsController;
import org.awdevelopment.smithlab.gui.controllers.TimepointsController;

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
        this.controller = controller;
        this.numTimepointsTextField = new ValidatableField(controller.numTimepointsTextField, controller.timepointsAddedLabel, FieldType.BYTE);
        this.numConditionsTextField = new ValidatableField(controller.numConditionsTextField, controller.numConditionsErrorLabel, FieldType.BYTE);
        this.numStrainsTextField = new ValidatableField(controller.numStrainsTextField, controller.numStrainErrorLabel, FieldType.BYTE);
        this.numReplicatesTextField = new ValidatableField(controller.numReplicatesTextField, controller.numReplicatesErrorLabelEmptyInputSheet, FieldType.BYTE);
        this.outputFilenameTextField = new ValidatableField(controller.outputFileTextField, controller.outputFilenameErrorLabel, FieldType.FILENAME);
        this.sampleLabelingRadioButtons = new ValidatableField(controller.sampleLabelingRadioButtons, controller.statusLabelEmptyInputSheets, FieldType.RADIO_BUTTONS);
        this.sampleSortingMethodChoiceBox = new ValidatableField(controller.sampleSortingMethodChoiceBox, controller.statusLabelEmptyInputSheets, FieldType.CHOICE_BOX);
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
    public Label getStatusLabel() { return controller.statusLabelEmptyInputSheets; }
    public Label getTimepointsAddedLabel() { return controller.timepointsAddedLabel; }
    public TimepointsController getTimepointsController() { return controller.timepointsController; }
    public StrainsController getStrainsController() { return controller.strainsController; }
    public ConditionsController getConditionsController() { return controller.conditionsController; }
}
