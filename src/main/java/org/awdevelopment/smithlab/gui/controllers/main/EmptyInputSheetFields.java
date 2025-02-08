package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.gui.controllers.ConditionsController;
import org.awdevelopment.smithlab.gui.controllers.StrainsController;
import org.awdevelopment.smithlab.gui.controllers.TimepointsController;

public class EmptyInputSheetFields extends AbstractFields {

    private final MainApplicationController controller;
    EmptyInputSheetFields(MainApplicationController controller) { this.controller = controller; }
    public TextField getNumTimepointsTextField() { return controller.numTimepointsTextField; }
    public Label getNumConditionsErrorLabel() { return controller.numConditionsErrorLabel; }
    public Label getNumTimepointsErrorLabel() { return controller.numTimepointsErrorLabel; }
    public Label getNumStrainErrorLabel() { return controller.numStrainErrorLabel; }
    public TextField getNumConditionsTextField() { return controller.numConditionsTextField; }
    public TextField getNumStrainsTextField() { return controller.numStrainsTextField; }
    public HBox getStrainsHBox() { return controller.strainsHBox; }
    public HBox getConditionsHBox() { return controller.conditionsHBox; }
    public TextField getNumReplicatesTextField() { return controller.numReplicatesEmptyInputSheetTextField; }
    public TextField getOutputFilenameTextField() { return controller.outputFilenameEmptyInputSheetsTextField; }
    public RadioButton getConditionLabelingRadioButton() { return controller.conditionLabelingRadioButton; }
    public RadioButton getStrainLabelingRadioButton() { return controller.strainLabelingRadioButton; }
    public RadioButton getConditionAndStrainLabelingRadioButton() { return controller.conditionAndStrainLabelingRadioButton; }
    public CheckBox getIncludeBaselineColumnCheckbox() { return controller.includeBaselineColumnCheckbox; }
    public ChoiceBox<SortOption> getSampleSortingMethodChoiceBox() { return controller.sampleSortingMethodEmptyInputSheetChoiceBox; }
    public Label getStatusLabel() { return controller.statusLabelEmptyInputSheets; }
    public Label getTimepointsAddedLabel() { return controller.timepointsAddedLabel; }
    public TimepointsController getTimepointsController() { return controller.timepointsController; }
    public StrainsController getStrainsController() { return controller.strainsController; }
    public ConditionsController getConditionsController() { return controller.conditionsController; }
}
