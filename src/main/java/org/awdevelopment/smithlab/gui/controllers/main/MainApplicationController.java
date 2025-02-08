package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;

public class MainApplicationController extends AbstractController {

    private ConfigManager config;

    // EMPTY INPUT SHEET
    @FXML protected Label numConditionsErrorLabel;
    @FXML protected Label numTimepointsErrorLabel;
    @FXML protected Label numStrainErrorLabel;
    @FXML protected TextField numConditionsTextField;
    @FXML protected TextField numStrainsTextField;
    @FXML protected HBox strainsHBox;
    @FXML protected HBox conditionsHBox;
    @FXML protected TextField numTimepointsTextField;
    @FXML protected TextField numReplicatesEmptyInputSheetTextField;
    @FXML protected TextField outputFilenameEmptyInputSheetsTextField;
    @FXML protected RadioButton conditionLabelingRadioButton;
    @FXML protected RadioButton strainLabelingRadioButton;
    @FXML protected RadioButton conditionAndStrainLabelingRadioButton;
    @FXML protected CheckBox includeBaselineColumnCheckbox;
    @FXML protected ChoiceBox<SortOption> sampleSortingMethodEmptyInputSheetChoiceBox;
    @FXML protected Label statusLabelEmptyInputSheets;
    @FXML protected Label timepointsAddedLabel;
    protected RadioButton[] sampleLabelingRadioButtons;
    protected TimepointsController timepointsController;
    protected StrainsController strainsController;
    protected ConditionsController conditionsController;

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
    public RadioButton[] radioButtons;

    private Mode mode = ConfigDefaults.MODE;
    GUILogger guiLogger;
    OutputSheetFields outputSheetFields;
    OutputSheetValidator outputSheetValidator;
    OutputSheetConfigUpdater outputSheetConfigUpdater;

    public MainApplicationController() {
        super();
    }

    public void initialize() {
        config = new ConfigManager(getLogger());
        guiLogger = new GUILogger(getLogger());
        // Set up the radio buttons and choice boxes
        radioButtonSetup();
        choiceBoxSetup();
        setupErrorLabelsOutputSheet();
        updateFields();
        // Initialize support classes for generating output sheets
        outputSheetFields = new OutputSheetFields(this);
        outputSheetValidator = new OutputSheetValidator(outputSheetFields, guiLogger, config.getOutputSheetsConfig());
        outputSheetConfigUpdater = new OutputSheetConfigUpdater(this, config.getOutputSheetsConfig());
    }

    private void choiceBoxSetup() {
        sampleSortingMethodChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodChoiceBox.setValue(config.getConfigValue(mode, ConfigOption.SORT_OPTION));
        sampleSortingMethodEmptyInputSheetChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodEmptyInputSheetChoiceBox.setValue(config.getConfigValue(mode, ConfigOption.SORT_OPTION));
    }

    private void radioButtonSetup() {
        radioButtons = new RadioButton[] {
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

    private void setupErrorLabelsOutputSheet() {
        replicatesErrorLabelOutputSheet.setText("");
        outputStyleErrorLabel.setText("");
        outputFilenameErrorLabel.setText("");
        inputFileExistsLabel.setText("");
        statusLabelOutputSheet.setText("");
        replicatesErrorLabelOutputSheet.setStyle("");
        outputStyleErrorLabel.setStyle("");
        outputFilenameErrorLabel.setStyle("");
        inputFileExistsLabel.setStyle("");
        statusLabelOutputSheet.setStyle("");
    }

    public void generateOutput() {
        switch (mode) {
            case GENERATE_OUTPUT_SHEETS -> {
                if (!outputSheetValidator.fieldsValid()) return;
                generateOutputSheets();
            }
            case GENERATE_EMPTY_INPUT_SHEET -> {
                if (notReadyForOutputEmptyInputSheet()) return;
                generateEmptyInputSheet();
            }
            case IMAGE_RECOGNITION -> {
                return;
            }
        }
    }

    private void generateOutputSheets() {
        getLogger().atDebug("FROM GUI: USER CLICKED GENERATE BUTTON");
        getLogger().atInfo("Generating output...");
        getLogger().atDebug(new String[] {
                "Input file: " + config.getConfigValue(mode, ConfigOption.INPUT_FILE),
                "Output file: " + config.getConfigValue(mode, ConfigOption.OUTPUT_FILE),
                "Output type: " + config.getConfigValue(mode, ConfigOption.OUTPUT_TYPE),
                "Write to different file: " + config.getConfigValue(mode, ConfigOption.WRITE_TO_DIFFERENT_FILE),
                "Sort option: " + config.getConfigValue(mode, ConfigOption.SORT_OPTION),
                "Number of replicates: " + config.getConfigValue(mode, ConfigOption.NUMBER_OF_REPLICATES),
                "Empty input sheet name: " + config.getConfigValue(mode, ConfigOption.EMPTY_INPUT_SHEET_NAME)
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

    public void updateSampleSortingMethod(ActionEvent actionEvent) { outputSheetConfigUpdater.updateSampleSortingMethod(); }

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

    public void updateFields() {
        outputSheetConfigUpdater.updateFields();
    }

    public void updateMode() {
        if (tabPane.getSelectionModel().getSelectedItem() == outputSheetsTab) {
            mode = Mode.GENERATE_OUTPUT_SHEETS;
        } else if (tabPane.getSelectionModel().getSelectedItem() == emptyInputSheetTab) {
            mode = Mode.GENERATE_EMPTY_INPUT_SHEET; }
//        else if (tabPane.getSelectionModel().getSelectedItem() == imageRecognitionTab) {
//            mode = Mode.IMAGE_RECOGNITION;
//        }
    }

    private boolean notReadyForOutputEmptyInputSheet() {
        if (badNumReplicatesEmptyInputSheet()) return true;
        else if (badNumTimepointsEmptyInputSheet()) return true;
        else if (badOutputFilenameEmptyInputSheet()) return true;
        else if (badSampleLabelingEmptyInputSheet()) return true;
        else if (badNumConditionsEmptyInputSheet()) return true;
        else if (badNumStrainsEmptyInputSheet()) return true;
        else return false;
    }

    private boolean badNumReplicatesEmptyInputSheet() {
        if (!checkNumReplicatesEmptyInputSheet()) return true;
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty()) {
            failedEmptyNumReplicatesEmptyInputSheet = true;
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of replicates");
            return true;
        }
        return false;
    }

    private boolean badNumTimepointsEmptyInputSheet() {
        if (!checkNumTimepointsEmptyInputSheet()) return true;
        if (numTimepointsTextField.getText().isEmpty()) {
            failedEmptyNumTimepointsEmptyInputSheet = true;
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of timepoints");
            return true;
        }
        return false;
    }

    private boolean badOutputFilenameEmptyInputSheet() {
        if (!checkOutputFilenameEmptyInputSheets()) return true;
        String outputFilename = outputFilenameEmptyInputSheetsTextField.getText();
        if (outputFilename.isEmpty()) {
            failedEmptyOutputFilenameEmptyInputSheet = true;
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter an output filename");
            return true;
        } else if (!outputFilename.endsWith(".xlsx")) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, "Error: Output filename must end in .xlsx");
            return true;
        }
        return false;
    }

    private boolean badSampleLabelingEmptyInputSheet() {
        return !checkSampleLabelingRadioButtons();
    }

    private boolean badNumConditionsEmptyInputSheet() {
        if (!checkNumConditionsEmptyInputSheet()) return true;
        if (numConditionsTextField.getText().isEmpty()) {
            failedEmptyConditionsEmptyInputSheet = true;
            guiLogger.errorOccurred(numConditionsErrorLabel, "Error: Please enter a number of conditions");
            return true;
        }
        return false;
    }

    private boolean badNumStrainsEmptyInputSheet() {
        if (!checkNumStrainsEmptyInputSheet()) return true;
        if (numStrainsTextField.getText().isEmpty()) {
            failedEmptyStrainsEmptyInputSheet = true;
            guiLogger.errorOccurred(numStrainErrorLabel, "Error: Please enter a number of strains");
            return true;
        }
        return false;
    }

    private void generateEmptyInputSheet() {
        OutputGenerator outputGenerator;
        try {
            outputGenerator = new OutputGenerator(config, getLogger());
        } catch (NoDaysException e) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, e.getMessage());
            return;
        }
        try {
            outputGenerator.generateEmptyInputSheet();
        } catch (OutputException e) {
            guiLogger.errorOccurred(statusLabelEmptyInputSheets, e.getMessage());
            return;
        }
        statusLabelEmptyInputSheets.setText("Successfully generated output!");
        statusLabelEmptyInputSheets.setStyle("-fx-text-fill: green");
    }

    public void emptyInputValidateFields() {
        String focusID = (outputFilenameEmptyInputSheetsTextField.getScene().getFocusOwner().getId());
        if (timepointsController != null) {
            config.setUsingNumDays(timepointsController.usingNumDays());
            config.setDays(timepointsController.getDays());
        }
        if (strainsController != null) {
            config.setUsingNumStrains(strainsController.usingNumStrains());
            config.setStrains(strainsController.getStrains());
        }
        if (conditionsController != null) {
            config.setUsingNumConditions(conditionsController.usingNumConditions());
            config.setConditions(conditionsController.getConditions());
        }
        if (focusID == null || focusID.isEmpty()) focusID = "default";
        switch (focusID) {
            case "numReplicatesEmptyInputSheetTextField" -> {
                checkOutputFilenameEmptyInputSheets();
                checkNumTimepointsEmptyInputSheet();
                checkNumConditionsEmptyInputSheet();
                checkNumStrainsEmptyInputSheet();
            }
            case "numTimepointsTextField" -> {
                checkOutputFilenameEmptyInputSheets();
                checkNumReplicatesEmptyInputSheet();
                checkNumConditionsEmptyInputSheet();
                checkNumStrainsEmptyInputSheet();
            }
            case "outputFilenameEmptyInputSheetsTextField" -> {
                checkNumReplicatesEmptyInputSheet();
                checkNumTimepointsEmptyInputSheet();
                checkNumConditionsEmptyInputSheet();
                checkNumStrainsEmptyInputSheet();
            }
            case "numConditionsTextField" -> {
                checkOutputFilenameEmptyInputSheets();
                checkNumReplicatesEmptyInputSheet();
                checkNumTimepointsEmptyInputSheet();
                checkNumStrainsEmptyInputSheet();
            }
            case "numStrainsTextField" -> {
                checkOutputFilenameEmptyInputSheets();
                checkNumReplicatesEmptyInputSheet();
                checkNumTimepointsEmptyInputSheet();
                checkNumConditionsEmptyInputSheet();
            }
            default -> {
                checkNumReplicatesEmptyInputSheet();
                checkOutputFilenameEmptyInputSheets();
                checkNumTimepointsEmptyInputSheet();
                checkNumConditionsEmptyInputSheet();
                checkNumStrainsEmptyInputSheet();
            }
        }
    }


    public void updateSampleLabelingRadioButtons(ActionEvent actionEvent) {
        emptyInputValidateFields();
        RadioButton selectedRadioButton = (RadioButton) actionEvent.getSource();
        for (RadioButton radioButton : sampleLabelingRadioButtons) if (!radioButton.equals(selectedRadioButton)) radioButton.setSelected(false);
        switch (selectedRadioButton.getId()) {
            case "conditionLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.CONDITION);
                strainsHBox.setDisable(true);
                conditionsHBox.setDisable(false);
            }
            case "strainLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.STRAIN);
                strainsHBox.setDisable(false);
                conditionsHBox.setDisable(true);
            }
            case "conditionAndStrainLabelingRadioButton" -> {
                config.setSampleLabelingType(SampleLabelingType.CONDITION_AND_STRAIN);
                strainsHBox.setDisable(false);
                conditionsHBox.setDisable(false);
            }
        }
        checkSampleLabelingRadioButtons();
    }

    public void openConditionsFXML() {
        emptyInputValidateFields();
        SceneLoader.loadScene(new Stage(), FXMLResourceType.CONDITIONS, getLogger(), config);
    }

    public void openStrainsFXML() {
        emptyInputValidateFields();
        SceneLoader.loadScene(new Stage(), FXMLResourceType.STRAINS, getLogger(), config);
    }

    public void openTimepointsFXML() {
        emptyInputValidateFields();
        Stage stage = new Stage();
        timepointsController = (TimepointsController) SceneLoader.loadScene(stage, FXMLResourceType.TIMEPOINTS, getLogger(), config);
    }

    public void updateNumReplicateEmptyInputSheet(KeyEvent keyEvent) {
        emptyInputValidateFields();
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty() && !failedEmptyNumReplicatesEmptyInputSheet) guiLogger.clearError(statusLabelEmptyInputSheets);
        if (    keyEvent == null
                || !(numReplicatesEmptyInputSheetTextField.getScene().focusOwnerProperty().get().getId().equals("numReplicatesEmptyInputSheetTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumReplicatesEmptyInputSheet();
        }
    }

    public void updateNumTimepointsEmptyInputSheet(KeyEvent keyEvent) {
        emptyInputValidateFields();
        if (numTimepointsTextField.getText().isEmpty() && !failedEmptyNumTimepointsEmptyInputSheet)
            guiLogger.clearError(statusLabelEmptyInputSheets);
        if (keyEvent == null
                || !(numTimepointsTextField.getScene().focusOwnerProperty().get().getId().equals("numTimepointsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumTimepointsEmptyInputSheet();
        }
    }

    public void updateNumConditionsEmptyInputSheet(KeyEvent keyEvent) {
        emptyInputValidateFields();
        if(numConditionsTextField.getText().isEmpty() && !failedEmptyConditionsEmptyInputSheet) guiLogger.clearError(statusLabelEmptyInputSheets);
        if (    keyEvent == null
                || !(numConditionsTextField.getScene().focusOwnerProperty().get().getId().equals("numConditionsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumConditionsEmptyInputSheet();
        }
    }

    public void updateNumStrainsEmptyInputSheet(KeyEvent keyEvent) {
        emptyInputValidateFields();
        if(numStrainsTextField.getText().isEmpty() && !failedEmptyStrainsEmptyInputSheet) guiLogger.clearError(statusLabelEmptyInputSheets);
        if (    keyEvent == null
                || !(numStrainsTextField.getScene().focusOwnerProperty().get().getId().equals("numStrainsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumStrainsEmptyInputSheet();
        }
    }

    public void handleIncludeBaselineColumn() {
        emptyInputValidateFields();
        config.setIncludeBaselineColumn(includeBaselineColumnCheckbox.isSelected()); }

}
