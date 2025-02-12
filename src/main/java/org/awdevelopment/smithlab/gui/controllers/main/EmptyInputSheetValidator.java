package org.awdevelopment.smithlab.gui.controllers.main;

import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.gui.controllers.AbstractLabelController;
import org.awdevelopment.smithlab.gui.controllers.ConditionsController;
import org.awdevelopment.smithlab.gui.controllers.StrainsController;
import org.awdevelopment.smithlab.gui.controllers.TimepointsController;

public class EmptyInputSheetValidator extends AbstractValidator {
    
    private final EmptyInputSheetFields fields;
    private final GUILogger guiLogger;
    private final EmptyInputSheetConfig config;

    public EmptyInputSheetValidator(EmptyInputSheetFields fields, GUILogger guiLogger, EmptyInputSheetConfig config) {
        super(config.LOGGER(), guiLogger, new ValidatableField[] {
                fields.getNumTimepointsTextField(),
                fields.getNumReplicatesTextField(),
                fields.getOutputFilenameTextField(),
                fields.getNumConditionsTextField(),
                fields.getNumStrainsTextField(),
                fields.getSampleSortingMethodChoiceBox(),
                fields.getSampleLabelingRadioButtons() });
        this.fields = fields;
        this.guiLogger = guiLogger;
        this.config = config;
    }

    void validateNumTimepoints() { validateControllerConnectedField(fields.getNumTimepointsTextField(), fields.getTimepointsController()); }

    void validateOutputFilename() { validateTextFieldFilename(fields.getOutputFilenameTextField()); }

    void validateNumReplicates() { validateTextFieldByte(fields.getNumReplicatesTextField()); }

    void validateNumConditions() {
        validateRadioButtonToggledField(fields.getNumConditionsTextField(),
                fields.getConditionsController(), "strainLabelingRadioButton");
    }

    void validateNumStrains() {
        validateRadioButtonToggledField(fields.getNumStrainsTextField(),
            fields.getStrainsController(), "conditionLabelingRadioButton");
    }

    @Override
    void validateTextFieldByID(String id) {
        switch (id) {
            case "numTimepointsTextField" -> validateNumTimepoints();
            case "outputFilenameEmptyInputSheetTextField" -> validateOutputFilename();
            case "numReplicatesEmptyInputSheetTextField" -> validateNumReplicates();
            case "numConditionsTextField" -> validateNumConditions();
            case "numStrainsTextField" -> validateNumStrains();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    void validateSampleLabelingRadioButtons() {
        if (!fields.getStrainLabelingRadioButton().isSelected()
                && !fields.getConditionLabelingRadioButton().isSelected()
                && !fields.getConditionAndStrainLabelingRadioButton().isSelected()) {
            guiLogger.errorOccurred(fields.getStatusLabel(), "Error: Please select a sample labeling option");
            fields.getSampleLabelingRadioButtons().setStatus(FieldStatus.INVALID);
        } else {
            guiLogger.clearError(fields.getStatusLabel());
            fields.getSampleLabelingRadioButtons().setStatus(FieldStatus.READY);
        }
    }

    private void validateRadioButtonToggledField(ValidatableField field, AbstractLabelController controller, String disableRadioButtonID) {
        if (field.getControl().getId().equals(disableRadioButtonID)) {
            field.setStatus(FieldStatus.READY);
            guiLogger.clearError(field.getErrorLabel());
        } else validateControllerConnectedField(field, controller);
    }

    private void validateControllerConnectedField(ValidatableField field, AbstractLabelController controller) {
        switch (controller) {
            case null -> { }
            case TimepointsController timepointsController -> {
                if (timepointsController.usingNumDays()) validateTextFieldByte(field);
                else {
                    guiLogger.clearError(field.getErrorLabel());
                    config.setUsingNumDays(timepointsController.usingNumDays());
                    config.setDays(timepointsController.getDays());
                    field.setStatus(FieldStatus.READY);
                }
            }
            case ConditionsController conditionsController -> {
                if (conditionsController.usingNumConditions()) validateTextFieldByte(field);
                else {
                    guiLogger.clearError(field.getErrorLabel());
                    config.setUsingNumConditions(conditionsController.usingNumConditions());
                    config.setConditions(conditionsController.getConditions());
                    field.setStatus(FieldStatus.READY);
                }
            }
            case StrainsController strainsController -> {
                if (strainsController.usingNumStrains()) validateTextFieldByte(field);
                else {
                    guiLogger.clearError(field.getErrorLabel());
                    config.setUsingNumStrains(strainsController.usingNumStrains());
                    config.setStrains(strainsController.getStrains());
                    field.setStatus(FieldStatus.READY);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + controller.getClass());
        }
    }
}
