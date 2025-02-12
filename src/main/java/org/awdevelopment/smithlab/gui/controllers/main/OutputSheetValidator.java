package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.ChoiceBox;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.config.SortOption;

public class OutputSheetValidator extends AbstractValidator {

    private final OutputSheetFields fields;

    protected OutputSheetValidator(OutputSheetFields fields, GUILogger guiLogger, OutputSheetsConfig config) {
        super(config.LOGGER(), guiLogger, new ValidatableField[] {
                fields.getInputFileTextField(),
                fields.getOutputFileTextField(),
                fields.getNumReplicatesTextField(),
                fields.getSampleSortingMethodChoiceBox(),
                fields.getRadioButtons() });
        this.fields = fields;
    }

    void inputFileTextFieldValid() { validateTextFieldFileExists(fields.getInputFileTextField()); }

    void outputFilenameTextFieldValid() {
        if (fields.getAddSheetsToInputFileCheckbox().isSelected()) fields.getOutputFileTextField().setStatus(FieldStatus.READY);
        else validateTextFieldFilename(fields.getOutputFileTextField());
    }

    void numReplicatesTextFieldValid() { validateTextFieldByte(fields.getNumReplicatesTextField()); }

    @SuppressWarnings("unchecked")
    void sampleSortingValid() {
        if (((ChoiceBox<SortOption>) fields.getSampleSortingMethodChoiceBox().getControl()).getValue() != null) {
            fields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.READY);
        } else fields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.INVALID);
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
