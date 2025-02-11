package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import org.awdevelopment.smithlab.config.SortOption;

public class OutputSheetFields extends AbstractFields{

    private ValidatableField inputFileTextField;
    private ValidatableField outputFileTextField;
    private ValidatableField numReplicatesTextField;
    private ValidatableField sampleSortingMethodChoiceBox;
    private ValidatableField outputStyleRadioButtons;

    private final MainApplicationController controller;

    OutputSheetFields(MainApplicationController controller) {
        this.controller = controller;
        this.inputFileTextField = new ValidatableField(controller.inputFileTextField);
        this.outputFileTextField = new ValidatableField(controller.outputFileTextField);
        this.numReplicatesTextField = new ValidatableField(controller.numReplicatesTextField);
        this.sampleSortingMethodChoiceBox = new ValidatableField(controller.sampleSortingMethodChoiceBox);
        this.outputStyleRadioButtons = new ValidatableField(controller.radioButtons);
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

    public TabPane getTabPane() {
        return controller.tabPane;
    }

    public Tab getOutputSheetsTab() {
        return controller.outputSheetsTab;
    }

    public Tab getEmptyInputSheetTab() {
        return controller.emptyInputSheetTab;
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
}

