package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class EmptyInputSheetConfigUpdater {

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

    public void handleIncludeBaselineColumn() {
        controller.emptyInputValidateFields();
        config.setIncludeBaselineColumn(fields.getIncludeBaselineColumnCheckbox().isSelected());
    }
}
