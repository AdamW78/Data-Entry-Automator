package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.awdevelopment.smithlab.config.*;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.gui.FXMLResourceType;
import org.awdevelopment.smithlab.gui.SceneLoader;
import org.awdevelopment.smithlab.gui.controllers.AbstractController;
import org.awdevelopment.smithlab.gui.controllers.ConditionsController;
import org.awdevelopment.smithlab.gui.controllers.StrainsController;
import org.awdevelopment.smithlab.gui.controllers.TimepointsController;
import org.awdevelopment.smithlab.gui.controllers.main.validatable_field.FieldStatus;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;
import org.awdevelopment.smithlab.logging.GUILogger;

public class MainApplicationController extends AbstractController {
    private ConfigManager config;

    // EMPTY INPUT SHEET
    @FXML protected Label numConditionsErrorLabel;
    @FXML protected Label numTimepointsErrorLabel;
    @FXML protected Label numStrainErrorLabel;
    @FXML protected Label numReplicatesErrorLabelEmptyInputSheet;
    @FXML protected TextField numConditionsTextField;
    @FXML protected TextField numStrainsTextField;
    @FXML protected HBox strainsHBox;
    @FXML protected HBox conditionsHBox;
    @FXML protected TextField numTimepointsTextField;
    @FXML protected TextField numReplicatesEmptyInputSheetTextField;
    @FXML protected TextField outputFilenameEmptyInputSheetTextField;
    @FXML protected RadioButton conditionLabelingRadioButton;
    @FXML protected RadioButton strainLabelingRadioButton;
    @FXML protected RadioButton conditionAndStrainLabelingRadioButton;
    @FXML protected CheckBox includeBaselineColumnCheckbox;
    @FXML protected ChoiceBox<SortOption> sampleSortingMethodEmptyInputSheetChoiceBox;
    @FXML protected Label statusLabelEmptyInputSheet;
    @FXML protected Label timepointsAddedLabel;
    protected RadioButton[] sampleLabelingRadioButtons;
    protected TimepointsController timepointsController;
    protected StrainsController strainsController;
    protected ConditionsController conditionsController;

    protected EmptyInputSheetFields emptyInputSheetFields;
    protected EmptyInputSheetValidator emptyInputSheetValidator;
    private EmptyInputSheetConfigUpdater emptyInputSheetConfigUpdater;

    // OUTPUT SHEETS
    @FXML protected Label statusLabelOutputSheet;
    @FXML protected Label replicatesErrorLabelOutputSheet;
    @FXML protected Label outputStyleErrorLabel;
    @FXML protected Label outputFilenameErrorLabel;
    @FXML protected TabPane tabPane;
    @FXML protected Tab outputSheetsTab;
    @FXML protected Tab emptyInputSheetTab;
    @FXML protected RadioButton outputStylePrismRadioButton;
    @FXML protected TextField inputFileTextField;
    @FXML protected Button inputFileBrowseButton;
    @FXML protected Label inputFileExistsLabel;
    @FXML protected RadioButton outputStyleTestsRadioButton;
    @FXML protected RadioButton outputStyleRawRadioButton;
    @FXML protected RadioButton outputStyleBothRadioButton;
    @FXML protected TextField outputFileTextField;
    @FXML protected CheckBox addSheetsToInputFileCheckbox;
    @FXML protected TextField numReplicatesTextField;
    @FXML protected ChoiceBox<SortOption> sampleSortingMethodChoiceBox;
    protected RadioButton[] outputStyleRadioButtons;

    private Mode mode = ConfigDefaults.MODE;
    GUILogger guiLogger;

    OutputSheetFields outputSheetFields;
    OutputSheetValidator outputSheetValidator;
    OutputSheetConfigUpdater outputSheetConfigUpdater;

    public MainApplicationController() {
        super();
    }

    public void initialize() {}

    public void setup() {
        config = new ConfigManager(getLogger());
        guiLogger = new GUILogger(getLogger());
        // Set up the radio buttons and choice boxes
        radioButtonSetup();
        choiceBoxSetup();
        // Initialize support classes for generating output sheets
        outputSheetFields = new OutputSheetFields(this);
        outputSheetValidator = new OutputSheetValidator(outputSheetFields, guiLogger, config.getOutputSheetsConfig());
        outputSheetConfigUpdater = new OutputSheetConfigUpdater(this, config.getOutputSheetsConfig());
        outputSheetFields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.READY);
        outputSheetFields.getRadioButtons().setStatus(FieldStatus.READY);
        // Initialize support classes for generating empty input sheets
        emptyInputSheetFields = new EmptyInputSheetFields(this);
        emptyInputSheetValidator = new EmptyInputSheetValidator(emptyInputSheetFields, guiLogger, config.getEmptyInputSheetConfig());
        emptyInputSheetConfigUpdater = new EmptyInputSheetConfigUpdater(this, config.getEmptyInputSheetConfig());
        emptyInputSheetFields.getSampleSortingMethodChoiceBox().setStatus(FieldStatus.READY);
        emptyInputSheetFields.getSampleLabelingRadioButtons().setStatus(FieldStatus.READY);
        setupErrorLabelsOutputSheet();
        setupErrorLabelsEmptyInputSheet();
    }

    private void choiceBoxSetup() {
        sampleSortingMethodChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodChoiceBox.setValue(config.getConfigValue(mode, ConfigOption.SORT_OPTION));
        sampleSortingMethodEmptyInputSheetChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodEmptyInputSheetChoiceBox.setValue(config.getConfigValue(mode, ConfigOption.SORT_OPTION));
    }

    private void radioButtonSetup() {
        outputStyleRadioButtons = new RadioButton[] {
                outputStylePrismRadioButton,
                outputStyleTestsRadioButton,
                outputStyleRawRadioButton,
                outputStyleBothRadioButton
        };
        sampleLabelingRadioButtons = new RadioButton[] {
                conditionLabelingRadioButton,
                strainLabelingRadioButton,
                conditionAndStrainLabelingRadioButton
        };
    }

    private void setupErrorLabel(Label label) {
        label.setText("");
        label.setStyle("");
    }

    private void setupErrorLabels(Label... labels) {
        for (Label label : labels) setupErrorLabel(label);
    }

    private void setupErrorLabelsOutputSheet() {
        setupErrorLabels(replicatesErrorLabelOutputSheet, outputStyleErrorLabel, outputFilenameErrorLabel,
                inputFileExistsLabel, statusLabelOutputSheet);
    }

    private void setupErrorLabelsEmptyInputSheet() {
        setupErrorLabels(numConditionsErrorLabel, numTimepointsErrorLabel, numStrainErrorLabel,
                numReplicatesErrorLabelEmptyInputSheet, statusLabelEmptyInputSheet);
    }

    public void generateOutput() {
        getLogger().atDebug("Mode: " + mode);
        switch (mode) {
            case GENERATE_OUTPUT_SHEETS -> {
                guiLogger.clearError(statusLabelOutputSheet);
                if (!outputSheetValidator.fieldsValid()) return;
                generateOutputSheets();
            }
            case GENERATE_EMPTY_INPUT_SHEET -> {
                guiLogger.clearError(statusLabelEmptyInputSheet);
                if (!emptyInputSheetValidator.fieldsValid()) return;
                generateEmptyInputSheet();
            }
            case IMAGE_RECOGNITION -> {}
        }
    }

    private void generateOutputSheets() {
        getLogger().atDebug("FROM GUI: USER CLICKED GENERATE BUTTON");
        getLogger().atDebug("Generating output...");
        getLogger().atDebug(new String[] {
                "Input file: " + config.getConfigValue(mode, ConfigOption.INPUT_FILE),
                "Output file: " + config.getConfigValue(mode, ConfigOption.OUTPUT_FILE),
                "Output type: " + config.getConfigValue(mode, ConfigOption.OUTPUT_TYPE),
                "Write to different file: " + config.getConfigValue(mode, ConfigOption.WRITE_TO_DIFFERENT_FILE),
                "Sort option: " + config.getConfigValue(mode, ConfigOption.SORT_OPTION),
                "Number of replicates: " + config.getConfigValue(mode, ConfigOption.NUMBER_OF_REPLICATES),
        });
        InputReader reader = new InputReader(config.getOutputSheetsConfig(), getLogger());
        getLogger().atDebug("Successfully initialized InputReader -  reading experiment data...");
        Experiment experiment;
        try {
            experiment = reader.readExperimentData();
        } catch (InputFileException e) {
            guiLogger.errorOccurred(statusLabelOutputSheet, e.getMessage());
            return;
        }
        getLogger().atDebug("Successfully read experiment data - Initializing OutputGenerator...");
        OutputGenerator outputGenerator = new OutputGenerator(config.getOutputSheetsConfig());
        getLogger().atDebug("Successfully initialized OutputGenerator - generating output...");
        try {
            outputGenerator.generateOutput(experiment);
        } catch (OutputException e) {
            guiLogger.errorOccurred(statusLabelOutputSheet, e.getMessage());
            return;
        }
        getLogger().atInfo("Successfully generated output!");
        setupErrorLabelsOutputSheet();
        statusLabelOutputSheet.setText("Successfully generated output!");
        statusLabelOutputSheet.setStyle("-fx-text-fill: green");
    }
    public void updateInputFile(KeyEvent keyEvent) { outputSheetConfigUpdater.updateInputFile(keyEvent); }

    public void browseForInputFile() { outputSheetConfigUpdater.browseForInputFile(); }

    public void handleRadioButtonPressOutputSheets(ActionEvent actionEvent) { outputSheetConfigUpdater.handleRadioButtonPressOutputSheets(actionEvent); }

    public void updateOutputFilename(KeyEvent keyEvent) { outputSheetConfigUpdater.updateOutputFilename(keyEvent); }

    public void handleAddSheetsCheckbox() { outputSheetConfigUpdater.handleAddSheetsCheckbox(); }

    public void updateNumReplicates(KeyEvent keyEvent) { outputSheetConfigUpdater.updateNumReplicates(keyEvent); }

    public void updateSampleSortingMethod() { if (outputSheetConfigUpdater != null) outputSheetConfigUpdater.updateSampleSortingMethod(); }

    public void updateOutputReplicatesFields() {
        outputSheetConfigUpdater.updateNumReplicates(null);
        outputSheetConfigUpdater.updateOutputFilename(null);
    }

    public void updateInputReplicatesFields() {
        outputSheetConfigUpdater.updateInputFile(null);
        outputSheetConfigUpdater.updateNumReplicates(null);
    }

    public void updateInputOutputFields() {
        outputSheetConfigUpdater.updateInputFile(null);
        outputSheetConfigUpdater.updateOutputFilename(null);
    }

    public void updateMode() {
        if (tabPane.getSelectionModel().getSelectedItem() == outputSheetsTab) {
            mode = Mode.GENERATE_OUTPUT_SHEETS;
        } else if (tabPane.getSelectionModel().getSelectedItem() == emptyInputSheetTab) {
            mode = Mode.GENERATE_EMPTY_INPUT_SHEET; }
//        else if (tabPane.getSelectionModel().getSelectedItem() == imageRecognitionTab) {
//            mode = Mode.IMAGE_RECOGNITION;
//        }
        switch (mode) {
            case GENERATE_OUTPUT_SHEETS -> { if (outputSheetConfigUpdater != null) updateFieldsOutputSheets();}
            case GENERATE_EMPTY_INPUT_SHEET -> { if (emptyInputSheetConfigUpdater != null) updateFieldsEmptyInputSheet(); }
            case IMAGE_RECOGNITION -> {}
        }
    }

    private void generateEmptyInputSheet() {
        OutputGenerator outputGenerator;
        try {
            outputGenerator = new OutputGenerator(config.getEmptyInputSheetConfig());
        } catch (NoDaysException | NoStrainsOrConditionsException e) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheet, e.getMessage());
            return;
        }
        try {
            outputGenerator.generateEmptyInputSheet();
        } catch (OutputException e) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheet, e.getMessage());
            return;
        }
        statusLabelEmptyInputSheet.setText("Successfully generated output!");
        statusLabelEmptyInputSheet.setStyle("-fx-text-fill: green");
    }

    public void updateFieldsEmptyInputSheet() {
        emptyInputSheetConfigUpdater.updateFields();
        emptyInputSheetValidator.preliminaryFieldsValid();
    }

    public void updateFieldsOutputSheets() {
        outputSheetConfigUpdater.updateFields();
        outputSheetValidator.preliminaryFieldsValid();
    }

    public void openConditionsFXML() {
        updateFieldsEmptyInputSheet();
        conditionsController = (ConditionsController) SceneLoader.loadScene(new Stage(), FXMLResourceType.CONDITIONS, getLogger(), config.getEmptyInputSheetConfig());
    }

    public void openStrainsFXML() {
        updateFieldsEmptyInputSheet();
        strainsController = (StrainsController) SceneLoader.loadScene(new Stage(), FXMLResourceType.STRAINS, getLogger(), config.getEmptyInputSheetConfig());
    }

    public void openTimepointsFXML() {
        updateFieldsEmptyInputSheet();
        timepointsController = (TimepointsController) SceneLoader.loadScene(new Stage(), FXMLResourceType.TIMEPOINTS, getLogger(), config.getEmptyInputSheetConfig());
    }

    public void updateSampleLabelingRadioButtons(ActionEvent actionEvent) { emptyInputSheetConfigUpdater.updateSampleLabelingRadioButtons(actionEvent); }

    public void updateNumConditionsEmptyInputSheet(KeyEvent keyEvent) { emptyInputSheetConfigUpdater.updateNumConditions(keyEvent); }

    public void updateNumStrainsEmptyInputSheet(KeyEvent keyEvent) { emptyInputSheetConfigUpdater.updateNumStrains(keyEvent); }

    public void updateNumReplicateEmptyInputSheet(KeyEvent keyEvent) { emptyInputSheetConfigUpdater.updateNumReplicates(keyEvent); }

    public void updateNumTimepointsEmptyInputSheet(KeyEvent keyEvent) { emptyInputSheetConfigUpdater.updateNumTimepoints(keyEvent); }

    public void handleIncludeBaselineColumn() { emptyInputSheetConfigUpdater.handleIncludeBaselineColumn(); }

    public void updateOutputFilenameEmptyInputSheet(KeyEvent keyEvent) { emptyInputSheetConfigUpdater.updateOutputFilename(keyEvent); }

    public Control getControlByID(String id) { return (Control) tabPane.getScene().lookup("#" + id); }
}
