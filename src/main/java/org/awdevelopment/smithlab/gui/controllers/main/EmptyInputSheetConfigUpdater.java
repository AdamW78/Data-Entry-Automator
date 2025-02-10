package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.ConfigOption;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.SampleLabelingType;

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
        controller.emptyInputValidateFields();
        if (fields.getNumReplicatesTextField().getText().isEmpty()&& !validator.failedEmptyNumReplicates())
            guiLogger.clearError(fields.getStatusLabel());
        if (    keyEvent == null
                || !(fields.getNumReplicatesTextField().getScene().focusOwnerProperty().get().getId().equals("numReplicatesEmptyInputSheetTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            validator.validateNumReplicates(false);
        }
    }

    public void updateNumTimepoints(KeyEvent keyEvent) {
        controller.emptyInputValidateFields();
        if (fields.getNumTimepointsTextField().getText().isEmpty() && !validator.failedEmptyNumTimepoints())
            guiLogger.clearError(fields.getStatusLabel());
        if (    keyEvent == null
                || !(fields.getNumTimepointsTextField().getScene().focusOwnerProperty().get().getId().equals("numTimepointsEmptyInputSheetTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            validator.validateNumTimepoints(false);
        }
    }

    public void updateNumConditions(KeyEvent keyEvent) {
        controller.emptyInputValidateFields();
        if(fields.getNumConditionsTextField().getText().isEmpty() && !validator.failedEmptyConditions()) guiLogger.clearError(fields.getStatusLabel());
        if (    keyEvent == null
                || !(fields.getNumConditionsTextField().getScene().focusOwnerProperty().get().getId().equals("numConditionsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            validator.validateNumConditions(false);
        }
    }

    public void updateNumStrains(KeyEvent keyEvent) {
        controller.emptyInputValidateFields();
        if(fields.getNumStrainsTextField().getText().isEmpty() && !validator.failedEmptyStrains()) guiLogger.clearError(fields.getStatusLabel());
        if (    keyEvent == null
                || !(fields.getNumStrainsTextField().getScene().focusOwnerProperty().get().getId().equals("numStrainsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            validator.validateNumStrains(false);
        }
    }

    public void updateSampleLabelingRadioButtons(ActionEvent actionEvent) {
        controller.emptyInputValidateFields();
        RadioButton selectedRadioButton = (RadioButton) actionEvent.getSource();
        for (RadioButton radioButton : fields.getSampleLabelingRadioButtons())
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

    public void updateSampleSortingMethod() {
        config.setSortOption(fields.getSampleSortingMethodChoiceBox().getValue());
    }

    @Override
    void updateTextField(TextField textField, ConfigOption option, KeyEvent keyEvent, Label errorLabel, boolean failedBoolean, boolean byteParse, boolean fileExists) {
        if (!byteParse && !fileExists) config.set(option, textField.getText());
        if (textField.getText().isEmpty() && !failedBoolean) guiLogger.clearError(errorLabel);
        if (keyEvent == null
                || !(textField.getScene().focusOwnerProperty().get().getId().equals(textField.getId()))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            boolean validatedSuccessfully = switch (textField.getId()) {
                case "numReplicatesEmptyInputSheetTextField" -> validator.validateNumReplicates(failedBoolean);
                case "outputFilenameEmptyInputSheetsTextField" -> validator.validateOutputFilename(failedBoolean);
                case "numConditionsTextField" -> validator.validateNumConditions(failedBoolean);
                case "numStrainsTextField" -> validator.validateNumStrains(failedBoolean);
                case "numTimepointsTextField" -> validator.validateNumTimepoints(failedBoolean);
                default -> throw new IllegalStateException("Unexpected value: " + textField.getId());
            };
            if (byteParse && validatedSuccessfully) config.set(option, Byte.parseByte(textField.getText()));
            if (fileExists && validatedSuccessfully) config.set(option, textField.getText());
        }
    }

    public void handleIncludeBaselineColumn() {
        controller.emptyInputValidateFields();
        config.setIncludeBaselineColumn(fields.getIncludeBaselineColumnCheckbox().isSelected());
    }
}
