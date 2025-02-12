package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.ConfigOption;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.config.SortOption;

public class EmptyInputSheetConfigUpdater extends AbstractConfigUpdater {

    private final EmptyInputSheetConfig config;
    private final EmptyInputSheetFields fields;
    private final EmptyInputSheetValidator validator;

    EmptyInputSheetConfigUpdater(MainApplicationController controller, EmptyInputSheetConfig config) {
        super(config, controller.emptyInputSheetValidator, controller.emptyInputSheetFields);
        this.config = config;
        this.fields = controller.emptyInputSheetFields;
        this.validator = controller.emptyInputSheetValidator;
    }

    public void updateFields() {
        updateNumConditions(null);
        updateNumReplicates(null);
        updateNumStrains(null);
        updateNumTimepoints(null);
        updateOutputFilename(null);
        updateSampleSortingMethod();
    }

    public void updateOutputFilename(KeyEvent keyEvent) {
        updateTextField(fields.getOutputFilenameTextField(), ConfigOption.OUTPUT_FILE, keyEvent);
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
        RadioButton selectedRadioButton = fields.getSelectedRadioButton();
        RadioButton[] radioButtons = new RadioButton[] { fields.getConditionLabelingRadioButton(), fields.getStrainLabelingRadioButton(), fields.getConditionAndStrainLabelingRadioButton() };
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.getId().equals(selectedRadioButton.getId())) {
                selectedRadioButton.setSelected(true);
                continue;
            }
            radioButton.setSelected(false);
        }
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
        String id = "sampleSortingMethodChoiceBox";
        ChoiceBox<SortOption> choiceBox;
        try {
            choiceBox = (ChoiceBox<SortOption>) fields.getControlByIDAndMode(id, config.mode());
        } catch (IllegalFieldAccessException e) {
            LOGGER().atFatal("Error occurred while updating sample sorting method choice box.", e);
            System.exit(1);
            return;
        }
        config.setSortOption(choiceBox.getValue());
    }

    public void handleIncludeBaselineColumn() {
        config.setIncludeBaselineColumn(fields.getIncludeBaselineColumnCheckbox().isSelected());
    }
}
