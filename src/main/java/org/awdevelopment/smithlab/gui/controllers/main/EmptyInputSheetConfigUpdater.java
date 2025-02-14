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
        RadioButton selectedRadioButton = actionEvent.getSource() instanceof RadioButton ? (RadioButton) actionEvent.getSource() : null;
        if (selectedRadioButton == null) {
            RadioButton previousSelectedRadioButton =
                    switch (config.sampleLabelingType()) {
                case CONDITION -> fields.getConditionLabelingRadioButton();
                case STRAIN -> fields.getStrainLabelingRadioButton();
                case CONDITION_AND_STRAIN -> fields.getConditionAndStrainLabelingRadioButton();
            };
            RadioButton[] selectedRadioButtons = new RadioButton[3];
            if (fields.getConditionLabelingRadioButton().isSelected()) selectedRadioButtons[0] = fields.getConditionLabelingRadioButton();
            if (fields.getStrainLabelingRadioButton().isSelected()) selectedRadioButtons[1] = fields.getStrainLabelingRadioButton();
            if (fields.getConditionAndStrainLabelingRadioButton().isSelected()) selectedRadioButtons[2] = fields.getConditionAndStrainLabelingRadioButton();
            if (selectedRadioButtons[0] == null && selectedRadioButtons[1] == null && selectedRadioButtons[2] == null) {
                LOGGER().atWarn("No radio button selected, setting previous radio button to selected.");
                selectedRadioButton = previousSelectedRadioButton;
            } else if (!(selectedRadioButtons[0] != null ^ selectedRadioButtons[1] != null ^ selectedRadioButtons[2] != null)) {
                LOGGER().atFatal("ERROR: More than one radio button selected, exiting...");
                System.exit(1);
                return;
            } else {
                for (RadioButton radioButton : selectedRadioButtons) {
                    if (radioButton != null) {
                        selectedRadioButton = radioButton;
                        break;
                    }
                }
                if (selectedRadioButton == null) {
                    // This should never happen
                    LOGGER().atFatal("ERROR: No radio button selected, exiting...");
                    System.exit(1);
                    return;
                }
            }
        }
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
        String id = "sampleSortingMethodEmptyInputSheetChoiceBox";
        ChoiceBox<SortOption> choiceBox;
        try {
            choiceBox = (ChoiceBox<SortOption>) fields.getControlByIDAndMode(id, config.mode());
        } catch (IllegalFieldAccessException e) {
            LOGGER().atFatal("Error occurred while updating sample sorting method choice box.", e);
            LOGGER().atFatal(e.getMessage());
            System.exit(1);
            return;
        }
        config.setSortOption(choiceBox.getValue());
    }

    public void handleIncludeBaselineColumn() {
        config.setIncludeBaselineColumn(fields.getIncludeBaselineColumnCheckbox().isSelected());
    }
}
