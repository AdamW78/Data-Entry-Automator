package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.FieldType;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.ValidatableField;

public class OutputSheetFields extends AbstractFields{

    private final ValidatableField inputFileTextField;
    private final ValidatableField outputFileTextField;
    private final ValidatableField numReplicatesTextField;
    private final ValidatableField sampleSortingMethodChoiceBox;
    private final ValidatableField outputStyleRadioButtons;

    private final MainApplicationController controller;

    OutputSheetFields(MainApplicationController controller) {
        super(controller, new ValidatableField[]{ new ValidatableField(controller.inputFileTextField, controller.inputFileExistsLabel, FieldType.EXISTING_FILE),
                new ValidatableField(controller.outputFileTextField, controller.outputFilenameErrorLabel, FieldType.FILENAME),
                new ValidatableField(controller.numReplicatesTextField, controller.replicatesErrorLabelOutputSheet, FieldType.BYTE),
                new ValidatableField(controller.sampleSortingMethodChoiceBox, controller.statusLabelOutputSheet, FieldType.CHOICE_BOX),
                new ValidatableField(controller.outputStyleRadioButtons, controller.outputStyleErrorLabel, FieldType.RADIO_BUTTONS)});
        this.controller = controller;
        this.inputFileTextField = super.getValidatableFields()[0];
        this.outputFileTextField = super.getValidatableFields()[1];
        this.numReplicatesTextField = super.getValidatableFields()[2];
        this.sampleSortingMethodChoiceBox = super.getValidatableFields()[3];
        this.outputStyleRadioButtons = super.getValidatableFields()[4];
    }

    public Label getStatusLabelOutputSheet() {
        return controller.statusLabelOutputSheet;
    }

    public Label getReplicatesErrorLabelOutputSheet() {
        return controller.replicatesErrorLabelOutputSheet;
    }

    public Label getOutputStyleErrorLabel() {
        return controller.outputStyleErrorLabel;
    }

    public Label getOutputFilenameErrorLabel() {
        return controller.outputFilenameErrorLabel;
    }

    public RadioButton getOutputStylePrismRadioButton() { return controller.outputStylePrismRadioButton; }

    public ValidatableField getInputFileTextField() { return inputFileTextField; }

    public Button getInputFileBrowseButton() {
        return controller.inputFileBrowseButton;
    }

    public Label getInputFileExistsLabel() {
        return controller.inputFileExistsLabel;
    }

    public RadioButton getOutputStyleTestsRadioButton() {
        return controller.outputStyleTestsRadioButton;
    }

    public RadioButton getOutputStyleRawRadioButton() {
        return controller.outputStyleRawRadioButton;
    }

    public RadioButton getOutputStyleBothRadioButton() {
        return controller.outputStyleBothRadioButton;
    }

    public ValidatableField getOutputFileTextField() {
        return outputFileTextField;
    }

    public CheckBox getAddSheetsToInputFileCheckbox() {
        return controller.addSheetsToInputFileCheckbox;
    }

    public ValidatableField getNumReplicatesTextField() {
        return numReplicatesTextField;
    }

    public ValidatableField getSampleSortingMethodChoiceBox() {
        return sampleSortingMethodChoiceBox;
    }

    public ValidatableField getRadioButtons() {
        return outputStyleRadioButtons;
    }

    public RadioButton getSelectedRadioButton() {
        String[] radioButtons = {"outputStylePrismRadioButton", "outputStyleTestsRadioButton", "outputStyleRawRadioButton", "outputStyleBothRadioButton"};
        RadioButton[] radioButtonArray = new RadioButton[radioButtons.length];
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtonArray[i] = (RadioButton) controller.getControlByID(radioButtons[i]);
        }
        for (RadioButton radioButton : radioButtonArray) {
            if (radioButton.isSelected()) {
                return radioButton;
            }
        }
        return null;
    }
}

