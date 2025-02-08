package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import org.awdevelopment.smithlab.config.SortOption;

public class OutputSheetFields extends AbstractFields{

    private final MainApplicationController controller;

    OutputSheetFields(MainApplicationController controller) {
        this.controller = controller;
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

    public RadioButton getOutputStylePrismRadioButton() {
        return controller.outputStylePrismRadioButton;
    }

    public TextField getInputFileTextField() {
        return controller.inputFileTextField;
    }

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

    public TextField getOutputFileTextField() {
        return controller.outputFileTextField;
    }

    public CheckBox getAddSheetsToInputFileCheckbox() {
        return controller.addSheetsToInputFileCheckbox;
    }

    public TextField getNumReplicatesTextField() {
        return controller.numReplicatesTextField;
    }

    public ChoiceBox<SortOption> getSampleSortingMethodChoiceBox() {
        return controller.sampleSortingMethodChoiceBox;
    }

    public RadioButton[] getRadioButtons() {
        return controller.radioButtons;
    }
}

