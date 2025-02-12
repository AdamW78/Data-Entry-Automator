package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.*;
import org.awdevelopment.smithlab.config.Mode;

public abstract class AbstractFields {

    private final MainApplicationController controller;
    private final ValidatableField[] validatableFields;

    AbstractFields(MainApplicationController controller, ValidatableField[] validatableFields) {
        this.controller = controller;
        this.validatableFields = validatableFields;
    }


    protected Control getControlByIDAndMode(String id, Mode mode) throws IllegalFieldAccessException {
        if (mode == Mode.GENERATE_OUTPUT_SHEETS) {
            return switch (id) {
                case "statusLabelOutputSheet", "replicatesErrorLabelOutputSheet", "outputStyleErrorLabel",
                     "outputFilenameErrorLabel", "tabPane", "outputSheetsTab", "emptyInputSheetTab",
                     "outputStylePrismRadioButton", "inputFileTextField", "inputFileBrowseButton",
                     "inputFileExistsLabel", "outputStyleTestsRadioButton", "outputStyleRawRadioButton",
                     "outputStyleBothRadioButton", "Field outputFileTextField", "addSheetsToInputFileCheckbox",
                     "numReplicatesTextField", "sampleSortingMethodChoiceBox" -> controller.getControlByID(id);
                default -> throw new IllegalFieldAccessException(mode, id);
            };
        } else if (mode == Mode.GENERATE_EMPTY_INPUT_SHEET) {
            return switch (id) {
                case "numConditionsErrorLabel", "numTimepointsErrorLabel", "numStrainErrorLabel", "tabPane",
                     "outputSheetsTab", "emptyInputSheetTab", "numReplicatesErrorLabelEmptyInputSheet",
                     "numConditionsTextField", "numStrainsTextField", "strainsHBox", "conditionsHBox",
                     "numTimepointsTextField", "numReplicatesEmptyInputSheetTextField",
                     "outputFilenameEmptyInputSheetTextField", "conditionLabelingRadioButton",
                     "strainLabelingRadioButton", "conditionAndStrainLabelingRadioButton",
                     "includeBaselineColumnCheckbox", "sampleSortingMethodEmptyInputSheetChoiceBox",
                     "statusLabelEmptyInputSheet", "timepointsAddedLabel" -> controller.getControlByID(id);
                default -> throw new IllegalFieldAccessException(mode, id);
            };

        }
        return controller.getControlByID(id);
    }

    public ValidatableField[] getValidatableFields() { return validatableFields; }
}
