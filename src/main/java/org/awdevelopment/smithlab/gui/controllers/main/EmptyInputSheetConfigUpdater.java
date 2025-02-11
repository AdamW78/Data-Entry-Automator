package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.ConfigOption;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.config.SortOption;

public class EmptyInputSheetConfigUpdater extends AbstractConfigUpdater {

    private final EmptyInputSheetConfig config;
    private final MainApplicationController controller;
    private final EmptyInputSheetFields fields;
    private final EmptyInputSheetValidator validator;
    private final GUILogger guiLogger;



    EmptyInputSheetConfigUpdater(MainApplicationController controller, EmptyInputSheetConfig config) {
        this.controller = controller;
        this.config = config;
        this.fields = controller.emptyInputSheetFields;
        this.validator = controller.emptyInputSheetValidator;
        this.guiLogger = controller.guiLogger;
    }

    public void updateFields() {
        updateNumConditions(null);
        updateNumReplicates(null);
        updateNumStrains(null);
        updateNumTimepoints(null);
        updateSampleSortingMethod();
    }

    public void updateNumReplicates(KeyEvent keyEvent) {
        updateTextField(fields.getNumReplicatesTextField(), ConfigOption.NUMBER_OF_REPLICATES, keyEvent);
    }

    public void updateNumTimepoints(KeyEvent keyEvent) {
        updateTextField(fields.getNumTimepointsTextField(), ConfigOption.NUM_DAYS, keyEvent);
    }

    public void updateNumConditions(KeyEvent keyEvent) {
        updateTextField(fields.getNumConditionsTextField(), ConfigOption.NUM_CONDITIONS, keyEvent);
    }

    public void updateNumStrains(KeyEvent keyEvent) {
        updateTextField(fields.getNumStrainsTextField(), ConfigOption.NUM_STRAINS, keyEvent);
    }

    public void updateSampleLabelingRadioButtons(ActionEvent actionEvent) {
        controller.emptyInputValidateFields();
        RadioButton selectedRadioButton = (RadioButton) actionEvent.getSource();
        for (RadioButton radioButton : ((RadioButton[]) fields.getSampleLabelingRadioButtons().getControls()))
            if (!radioButton.equals(selectedRadioButton)) radioButton.setSelected(false);
        switch (selectedRadioButton.getId()) {
            case "conditionLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.CONDITION);
                fields.getStrainsHBox().setDisable(true);
                fields.getConditionsHBox().setDisable(false);
            }
            case "strainLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.STRAIN);
                fields.getStrainsHBox().setDisable(false);
                fields.getConditionsHBox().setDisable(true);
            }
            case "conditionAndStrainLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.CONDITION_AND_STRAIN);
                fields.getStrainsHBox().setDisable(false);
                fields.getConditionsHBox().setDisable(false);
            }
        }
        validator.validateSampleLabelingRadioButtons();
    }

    @SuppressWarnings("unchecked")
    public void updateSampleSortingMethod() {
        config.setSortOption(((ChoiceBox<SortOption>) fields.getSampleSortingMethodChoiceBox().getControl()).getValue());
    }

    @Override
    void updateTextField(ValidatableField field, ConfigOption option, KeyEvent keyEvent) {
        controller.emptyInputValidateFields();
        TextField textField = (TextField) field.getControl();
        if (textField.getText().isEmpty() && !field.hasFailedEmptyCheck()) guiLogger.clearError(field.getErrorLabel());
        if (keyEvent != null) { field.touch(); }
        if (keyEvent == null
                || !(textField.getScene().focusOwnerProperty().get().getId().equals(textField.getId()))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            boolean validatedSuccessfully = switch (textField.getId()) {
                case "numReplicatesEmptyInputSheetTextField" -> validator.validateNumReplicates(field.hasFailedEmptyCheck());
                case "outputFilenameEmptyInputSheetsTextField" -> validator.validateOutputFilename(field.hasFailedEmptyCheck());
                case "numConditionsTextField" -> validator.validateNumConditions(field.hasFailedEmptyCheck());
                case "numStrainsTextField" -> validator.validateNumStrains(field.hasFailedEmptyCheck());
                case "numTimepointsTextField" -> validator.validateNumTimepoints(field.hasFailedEmptyCheck());
                default -> throw new IllegalStateException("Unexpected value: " + textField.getId());
            };
            if (validatedSuccessfully) {
                switch (field.getFieldType()) {
                    case BYTE -> config.set(option, Byte.parseByte(textField.getText()));
                    case FILENAME, STRING -> config.set(option, textField.getText());
                    default -> throw new IllegalStateException("Unexpected value: " + field.getFieldType());}
            }
        }
    }

    public void handleIncludeBaselineColumn() {
        controller.emptyInputValidateFields();
        config.setIncludeBaselineColumn(fields.getIncludeBaselineColumnCheckbox().isSelected());
    }
}
