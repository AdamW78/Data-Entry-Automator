package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;

public class OutputSheetFields extends AbstractFields{

    private final ValidatableField inputFileTextField;
    private final ValidatableField outputFileTextField;
    private final ValidatableField numReplicatesTextField;
    private final ValidatableField sampleSortingMethodChoiceBox;
    private final ValidatableField outputStyleRadioButtons;

    private final MainApplicationController controller;

    OutputSheetFields(MainApplicationController controller) {
        this.controller = controller;
        this.inputFileTextField = new ValidatableField(controller.inputFileTextField, controller.inputFileExistsLabel, FieldType.EXISTING_FILE);
        this.outputFileTextField = new ValidatableField(controller.outputFileTextField, controller.outputFilenameErrorLabel, FieldType.FILENAME);
        this.numReplicatesTextField = new ValidatableField(controller.numReplicatesTextField, controller.replicatesErrorLabelOutputSheet, FieldType.BYTE);
        this.sampleSortingMethodChoiceBox = new ValidatableField(controller.sampleSortingMethodChoiceBox, controller.statusLabelOutputSheet, FieldType.CHOICE_BOX);
        this.outputStyleRadioButtons = new ValidatableField(controller.outputStyleRadioButtons, controller.outputStyleErrorLabel, FieldType.RADIO_BUTTONS);
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
        for (RadioButton radioButton : ((RadioButton[]) outputStyleRadioButtons.getControls())) {
            if (radioButton.isSelected()) {
                return radioButton;
            }
        }
        return null;
    }
}

