package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.ChoiceBox;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.config.SortOption;

public class OutputSheetValidator extends AbstractValidator {

    private final OutputSheetFields fields;
    private final OutputSheetsConfig config;

    protected OutputSheetValidator(OutputSheetFields fields, GUILogger guiLogger, OutputSheetsConfig config) {
        super(config.LOGGER(), guiLogger, fields, config.mode());
        this.fields = fields;
        this.config = config;
    }

    void inputFileTextFieldValid() { validateTextFieldFileExists(fields.getInputFileTextField()); }

    void outputFilenameTextFieldValid() {
        if (fields.getAddSheetsToInputFileCheckbox().isSelected()) fields.getOutputFileTextField().setStatus(FieldStatus.READY);
        else validateTextFieldFilename(fields.getOutputFileTextField());
    }

    void numReplicatesTextFieldValid() { validateTextFieldByte(fields.getNumReplicatesTextField()); }

    @SuppressWarnings("unchecked")
    void sampleSortingValid() {
        try {
            if (((ChoiceBox<SortOption>) fields.getControlByIDAndMode("sampleSortingMethodChoiceBox", config.mode())).getValue() != null) {
                fields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.READY);
            } else fields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.INVALID);
        } catch (IllegalFieldAccessException e) {
            LOGGER().atError("Error occurred while validating sample sorting method choice box.");
            LOGGER().atError(e.getMessage());
            System.exit(1);
        }
    }

    void outputTypeValid() {
        if (fields.getOutputStylePrismRadioButton().isSelected()
                ^ fields.getOutputStyleTestsRadioButton().isSelected()
                ^ fields.getOutputStyleRawRadioButton().isSelected()
                ^ fields.getOutputStyleBothRadioButton().isSelected()) {
            fields.getRadioButtons().setStatus(FieldStatus.READY);
        } else fields.getRadioButtons().setStatus(FieldStatus.INVALID);
    }

    @Override
    void validateTextFieldByID(String id) {
        switch (id) {
            case "inputFileTextField" -> inputFileTextFieldValid();
            case "outputFileTextField" -> outputFilenameTextFieldValid();
            case "numReplicatesTextField" -> numReplicatesTextFieldValid();
            case "sampleSortingMethodChoiceBox" -> sampleSortingValid();
            case "radioButtons" -> outputTypeValid();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        }
    }
}
