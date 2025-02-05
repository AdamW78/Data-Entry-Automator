package org.awdevelopment.smithlab.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.gui.FXMLResourceType;
import org.awdevelopment.smithlab.gui.SceneLoader;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class MainApplicationController extends AbstractController {

    private final Config config;

    private TimepointsController timepointsController;
    // EMPTY INPUT SHEET
    @FXML
    private HBox strainsHBox;
    @FXML
    private HBox conditionsHBox;
    @FXML
    private TextField numTimepointsTextField;
    @FXML
    private TextField numReplicatesEmptyInputSheetTextField;
    @FXML
    private TextField outputFilenameEmptyInputSheetsTextField;
    @FXML
    private RadioButton conditionLabelingRadioButton;
    @FXML
    private RadioButton strainLabelingRadioButton;
    @FXML
    private RadioButton conditionAndStrainLabelingRadioButton;
    @FXML
    private CheckBox includeBaselineColumnCheckbox;
    @FXML
    private ChoiceBox<SortOption> sampleSortingMethodEmptyInputSheetChoiceBox;
    @FXML
    private Label statusLabelEmptyInputSheets;
    @FXML
    private Label timepointsAddedLabel;
    // OUTPUT SHEETS
    @FXML
    private Label statusLabelOutputSheet;
    @FXML
    private Label replicatesErrorLabelOutputSheet;
    @FXML
    private Label outputStyleErrorLabel;
    @FXML
    private Label outputFilenameErrorLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab outputSheetsTab;
    @FXML
    private Tab emptyInputSheetTab;
    @FXML
    private RadioButton outputStylePrismRadioButton;
    @FXML
    private TextField inputFileTextField;
    @FXML
    private Button inputFileBrowseButton;
    @FXML
    private Label inputFileExistsLabel;
    @FXML
    private RadioButton outputStyleTestsRadioButton;
    @FXML
    private RadioButton outputStyleRawRadioButton;
    @FXML
    private RadioButton outputStyleBothRadioButton;
    @FXML
    private TextField outputFileTextField;
    @FXML
    private CheckBox addSheetsToInputFileCheckbox;
    @FXML
    private TextField numReplicatesTextField;
    @FXML
    private ChoiceBox<SortOption> sampleSortingMethodChoiceBox;

    private RadioButton[] radioButtons;
    private RadioButton[] sampleLabelingRadioButtons;

    private boolean failedEmptyReplicates = false;
    private boolean failedEmptyOutputFilename = false;
    private boolean failedEmptyInputFile = false;
    private boolean failedEmptyNumTimepointsEmptyInputSheet = false;
    private boolean failedEmptyNumReplicatesEmptyInputSheet = false;
    private boolean failedEmptyOutputFilenameEmptyInputSheet = false;

    public MainApplicationController() {
        super();
        config = new Config();
    }

    public void initialize() {
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
        sampleSortingMethodChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodChoiceBox.setValue(config.sortOption());
        sampleSortingMethodEmptyInputSheetChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodEmptyInputSheetChoiceBox.setValue(config.sortOption());
        setupErrorLabelsOutputSheet();
        updateFields();
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
        if (config.mode() == Mode.GENERATE_OUTPUT_SHEETS) {
            if (notReadyForOutputOutputSheet()) return;
            generateOutputSheets();
        }
        else if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) {
            if (notReadyForOutputEmptyInputSheet()) return;
            generateEmptyInputSheet();
        }
    }


    private void generateOutputSheets() {
        getLogger().atDebug("FROM GUI: USER CLICKED GENERATE BUTTON");
        getLogger().atInfo("Generating output...");
        getLogger().atDebug(new String[] {
                "Input file: " + config.inputFile().getPath(),
                "Output file: " + config.outputFilename(),
                "Output type: " + config.outputType(),
                "Write to different file: " + config.writeToDifferentFile(),
                "Sort option: " + config.sortOption(),
                "Number of replicates: " + config.numReplicates(),
                "Empty input sheet name: " + config.emptyInputSheetName()
        });
        InputReader reader = new InputReader(config, getLogger());
        getLogger().atDebug("Successfully initialized InputReader -  reading experiment data...");
        Experiment experiment;
        try {
            experiment = reader.readExperimentData();
        } catch (InputFileException e) {
            errorOccurred(statusLabelOutputSheet, e.getMessage());
            return;
        }
        getLogger().atDebug("Successfully read experiment data - Initializing OutputGenerator...");
        OutputGenerator outputGenerator;
        try {
            outputGenerator = new OutputGenerator(config, getLogger());
        } catch (NoDaysException e) {
            // SHOULD NEVER HAPPEN
            throw new RuntimeException(e);
        }
        getLogger().atDebug("Successfully initialized OutputGenerator - generating output...");
        try {
            outputGenerator.generateOutput(experiment);
        } catch (OutputException e) {
            errorOccurred(statusLabelOutputSheet, e.getMessage());
            return;
        }
        getLogger().atInfo("Successfully generated output!");
        failedEmptyInputFile = false;
        failedEmptyOutputFilename = false;
        failedEmptyReplicates = false;
        setupErrorLabelsOutputSheet();
        statusLabelOutputSheet.setText("Successfully generated output!");
        statusLabelOutputSheet.setStyle("-fx-text-fill: green");
    }

    private boolean notReadyForOutputOutputSheet() {
        if (sampleSortingMethodChoiceBox.getValue() == null) config.setSortOption(SortOption.NONE);
        if (badInputFileField()) return true;
        if (badOutputStyleRadioButton()) return true;
        if (badOutputFilenameTextField()) return true;
        if (badNumReplicatesTextField()) return true;
        return false;
    }

    private boolean badInputFileField() {
        if (!checkInputFile()) return true;
        if (inputFileExistsLabel.getText().isEmpty()) {
            failedEmptyInputFile = true;
            errorOccurred(inputFileExistsLabel, "Error: Please enter an input file path");
            return true;
        }
        return false;
    }

    private boolean badOutputStyleRadioButton() {
        if (outputStylePrismRadioButton.isSelected()) {
            config.setOutputType(OutputType.PRISM);
        } else if (outputStyleTestsRadioButton.isSelected()) {
            config.setOutputType(OutputType.STATISTICAL_TESTS);
        } else if (outputStyleRawRadioButton.isSelected()) {
            config.setOutputType(OutputType.RAW);
        } else if (outputStyleBothRadioButton.isSelected()) {
            config.setOutputType(OutputType.BOTH);
        } else {
            errorOccurred(outputStyleErrorLabel, "Error: Please select an output style");
            return true;
        }
        return false;
    }

    private boolean badOutputFilenameTextField() {
        if (!checkOutputFilenameOutputSheets()) return true;
        String outputFilename = outputFileTextField.getText();
        if (outputFilename.isEmpty() && !addSheetsToInputFileCheckbox.isSelected()) {
            failedEmptyOutputFilename = true;
            errorOccurred(outputFilenameErrorLabel, "Error: Please enter an output filename");
            return true;
        }
        return false;
    }

    private boolean badNumReplicatesTextField() {
        if (!checkNumReplicatesTextField()) {
            System.out.println("badNumReplicatesTextField - checkNumReplicatesTextField() returned false");
        return true;
        }
        if (numReplicatesTextField.getText().isEmpty()
            && (config.outputType() == OutputType.STATISTICAL_TESTS
                || config.outputType() == OutputType.BOTH)) {
                failedEmptyReplicates = true;
                errorOccurred(replicatesErrorLabelOutputSheet, "Error: Please enter a number of replicates");
                return true;
        }
        return false;
    }

    private boolean checkInputFile() {
        if (inputFileTextField.getText().isEmpty() && !failedEmptyInputFile) {
            clearError(inputFileExistsLabel);
            return true;
        } else if (inputFileTextField.getText().isEmpty()) {
            errorOccurred(inputFileExistsLabel, "Error: Please enter an input file path");
            return false;
        }
        File inputFile = new File(inputFileTextField.getText());
        if (inputFile.exists()) {
            if (inputFile.isDirectory()) {
                errorOccurred(inputFileExistsLabel, "Error: Input file \"" + inputFileTextField.getText() + "\" is a directory");
                return false;
            } else {
                inputFileExistsLabel.setText("Input file exists!");
                inputFileExistsLabel.setStyle("-fx-text-fill: green");
                return true;
            }
        } else {
            errorOccurred(inputFileExistsLabel, "Error: Input file \"" + inputFileTextField.getText() + "\" does not exist");
            return false;
        }
    }

    private boolean checkNumReplicatesTextField() {
        if ((numReplicatesTextField.getText().isEmpty() && !failedEmptyReplicates)
                || (config.outputType() == OutputType.PRISM || config.outputType() == OutputType.RAW)) {
            clearError(replicatesErrorLabelOutputSheet);
            return true;
        }
        else if (numReplicatesTextField.getText().isEmpty() && (config.outputType() == OutputType.STATISTICAL_TESTS || config.outputType() == OutputType.BOTH)) {
            errorOccurred(replicatesErrorLabelOutputSheet, "Error: Please enter a number of replicates");
            return false;
        }
        try {
            long numReplicates = Long.parseLong(numReplicatesTextField.getText());
            if (numReplicates < 1) {
                errorOccurred(replicatesErrorLabelOutputSheet, "Error: Number must be > 0");
                return false;
            } else if (numReplicates > 127) {
                errorOccurred(replicatesErrorLabelOutputSheet, "Error: Number must be <= 127");
                return false;
            } else {
                clearError(replicatesErrorLabelOutputSheet);
                config.setNumberOfReplicates((byte) numReplicates);
                return true;
            }
        } catch (NumberFormatException e) {
            errorOccurred(replicatesErrorLabelOutputSheet, "Error: Invalid number: \"" + numReplicatesTextField.getText() + "\"");
            return false;
        }
    }

    private boolean checkOutputFilenameOutputSheets() {
        if (addSheetsToInputFileCheckbox.isSelected()) {
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(inputFileTextField.getText());
            return true;
        } else return checkOutputFilename(outputFileTextField, failedEmptyOutputFilename, outputFilenameErrorLabel);
    }

    public void browseForInputFile() {
        getLogger().atDebug("Browsing for input file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input File (ending in .xlsx)");
        File inputFile = fileChooser.showOpenDialog(inputFileBrowseButton.getScene().getWindow());
        if (inputFile != null) {
            getLogger().atDebug("Selected input file: " + inputFile.getAbsolutePath());
            inputFileTextField.setText(inputFile.getAbsolutePath());
            updateInputFile(null);
        }
    }

    public void handleRadioButtonPress(ActionEvent actionEvent) {
        updateFields();
        RadioButton radioButton = (RadioButton) actionEvent.getSource();
        for (RadioButton otherRadioButton : radioButtons) if (!otherRadioButton.equals(radioButton)) otherRadioButton.setSelected(false);
        OutputType oldOutputType = config.outputType();
        if (outputStylePrismRadioButton.isSelected()) config.setOutputType(OutputType.PRISM);
        else if (outputStyleTestsRadioButton.isSelected()) config.setOutputType(OutputType.STATISTICAL_TESTS);
        else if (outputStyleRawRadioButton.isSelected()) config.setOutputType(OutputType.RAW);
        else if (outputStyleBothRadioButton.isSelected()) config.setOutputType(OutputType.BOTH);
        getLogger().atTrace("Radio button press ActionEvent caught: Switched from OutputType \""
                + oldOutputType +"\" to new OutputType \"" + radioButton.getText() + "\".");
    }

    public void handleAddSheetsCheckbox() {
        if (addSheetsToInputFileCheckbox.isSelected()) {
            outputFileTextField.setDisable(true);
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(inputFileTextField.getText());
            if (!outputFilenameErrorLabel.getText().isEmpty()) clearError(outputFilenameErrorLabel);
        } else {
            outputFileTextField.setDisable(false);
            config.setWriteToDifferentFile(true);
            config.setOutputFilename(outputFileTextField.getText());
        }
    }

    public void updateMode() {
        if (tabPane.getSelectionModel().getSelectedItem() == outputSheetsTab) {
            config.setMode(Mode.GENERATE_OUTPUT_SHEETS);
        } else if (tabPane.getSelectionModel().getSelectedItem() == emptyInputSheetTab) {
            config.setMode(Mode.GENERATE_EMPTY_INPUT_SHEET);
        }
    }

    public void updateFields() {
        updateMode();
        updateInputFile(null);
        updateOutputFilename(null);
        updateSampleSortingMethod();
    }

    public void updateNumReplicates(KeyEvent keyEvent) {
        if (numReplicatesTextField.getText().isEmpty() && !failedEmptyReplicates) clearError(replicatesErrorLabelOutputSheet);
        if (    keyEvent == null
                || !(numReplicatesTextField.getScene().focusOwnerProperty().get().getId().equals("numReplicatesTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumReplicatesTextField();
        }
    }


    public void updateOutputFilename(KeyEvent keyEvent) {
        config.setOutputFilename(outputFileTextField.getText());
        if (outputFileTextField.getText().isEmpty() && !failedEmptyOutputFilename) clearError(outputFilenameErrorLabel);
        if (    keyEvent == null
                || !(outputFileTextField.getScene().focusOwnerProperty().get().getId().equals("outputFileTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkOutputFilenameOutputSheets();
        }
    }


    public void updateSampleSortingMethod() { config.setSortOption(sampleSortingMethodChoiceBox.getValue());}

    public void updateInputFile(KeyEvent keyEvent) {
        File inputFile = new File(inputFileTextField.getText());
        config.setInputFile(inputFile);
        if (inputFileTextField.getText().isEmpty() && !failedEmptyInputFile) clearError(inputFileExistsLabel);
        if (    keyEvent == null
                || !(inputFileTextField.getScene().focusOwnerProperty().get().getId().equals("inputFileTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkInputFile();
        }
    }

    public void updateOutputReplicatesFields() {
        updateOutputFilename(null);
        updateNumReplicates(null);
    }

    public void updateInputReplicatesFields() {
        updateInputFile(null);
        updateNumReplicates(null);
    }

    public void updateInputOutputFields() {
        updateInputFile(null);
        updateOutputFilename(null);
    }

    private void clearError(Label errorLabel) {
        errorLabel.setText("");
        errorLabel.setStyle("");
    }

    private void errorOccurred(Label errorLabel, String errorMessage) {
        getLogger().atError(errorMessage);
        errorLabel.setText(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red");
    }

    private boolean notReadyForOutputEmptyInputSheet() {
        if (badNumReplicatesEmptyInputSheet()) return true;
        if (badNumTimepointsEmptyInputSheet()) return true;
        if (badOutputFilenameEmptyInputSheet()) return true;
        if (badSampleLabelingEmptyInputSheet()) return true;
        return false;

    }

    private boolean badNumReplicatesEmptyInputSheet() {
        if (!checkNumReplicatesEmptyInputSheet()) return true;
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty()) {
            failedEmptyNumReplicatesEmptyInputSheet = true;
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of replicates");
            return true;
        }
        return false;
    }

    private boolean badNumTimepointsEmptyInputSheet() {
        if (!checkNumTimepointsEmptyInputSheet()) return true;
        if (numTimepointsTextField.getText().isEmpty()) {
            failedEmptyNumTimepointsEmptyInputSheet = true;
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of timepoints");
            return true;
        }
        return false;
    }

    private boolean badOutputFilenameEmptyInputSheet() {
        if (!checkOutputFilenameEmptyInputSheets()) return true;
        String outputFilename = outputFilenameEmptyInputSheetsTextField.getText();
        if (outputFilename.isEmpty()) {
            failedEmptyOutputFilenameEmptyInputSheet = true;
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter an output filename");
            return true;
        } else if (!outputFilename.endsWith(".xlsx")) {
            errorOccurred(statusLabelEmptyInputSheets, "Error: Output filename must end in .xlsx");
            return true;
        }
        return false;
    }

    private boolean badSampleLabelingEmptyInputSheet() {
        return !checkSampleLabelingRadioButtons();
    }

    private void generateEmptyInputSheet() {
        OutputGenerator outputGenerator;
        try {
            outputGenerator = new OutputGenerator(config, getLogger());
        } catch (NoDaysException e) {
            errorOccurred(statusLabelEmptyInputSheets, e.getMessage());
            return;
        }
        try {
            outputGenerator.generateEmptyInputSheet();
        } catch (OutputException e) {
            errorOccurred(statusLabelEmptyInputSheets, e.getMessage());
            return;
        }
        statusLabelEmptyInputSheets.setText("Successfully generated output!");
        statusLabelEmptyInputSheets.setStyle("-fx-text-fill: green");
    }

    public void emptyInputValidateFields() {
        String focusID = (outputFilenameEmptyInputSheetsTextField.getScene().getFocusOwner().getId());
        if (focusID == null || focusID.isEmpty()) focusID = "default";
        if (focusID.equals("numReplicatesEmptyInputSheetTextField")) {
                checkOutputFilenameEmptyInputSheets();
                checkNumTimepointsEmptyInputSheet();
        } else if (focusID.equals("numTimepointsTextField")) {
            checkOutputFilenameEmptyInputSheets();
            checkNumReplicatesEmptyInputSheet();
        } else if (focusID.equals("outputFilenameEmptyInputSheetsTextField")) {
            checkNumReplicatesEmptyInputSheet();
            checkNumTimepointsEmptyInputSheet();
        } else {
            checkNumReplicatesEmptyInputSheet();
            checkOutputFilenameEmptyInputSheets();
            checkNumTimepointsEmptyInputSheet();
        }
    }

    private boolean checkNumTimepointsEmptyInputSheet() {
        if (timepointsController == null || timepointsController.usingNumDays()) {
            if (numTimepointsTextField.getText().isEmpty() && failedEmptyNumTimepointsEmptyInputSheet) {
                errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of timepoints");
                return false;
            } else if (numTimepointsTextField.getText().isEmpty()) {
                clearError(statusLabelEmptyInputSheets);
                return true;
            } else {
                try {
                    long numDays = Long.parseLong(numTimepointsTextField.getText());
                    if (numDays < 1) {
                        errorOccurred(statusLabelEmptyInputSheets, "Error: Number of timepoints must be > 0");
                        return false;
                    } else if (numDays > 127) {
                        errorOccurred(statusLabelEmptyInputSheets, "Error: Number of timepoints must be <= 127");
                        return false;
                    } else {
                        clearError(statusLabelEmptyInputSheets);
                        config.setNumDays((byte) numDays);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    errorOccurred(statusLabelEmptyInputSheets, "Error: Invalid number: \"" + numTimepointsTextField.getText() + "\"");
                    return false;
                }
            }
        } else return true;
    }

    private boolean checkOutputFilenameEmptyInputSheets() {
        return checkOutputFilename(outputFilenameEmptyInputSheetsTextField, failedEmptyOutputFilenameEmptyInputSheet, statusLabelEmptyInputSheets);
    }

    private boolean checkOutputFilename(TextField textField, boolean failedEmptyBoolean, Label errorLabel) {
        if (outputFilenameEmptyInputSheetsTextField.getText().isEmpty() && !failedEmptyOutputFilenameEmptyInputSheet) {
            clearError(statusLabelEmptyInputSheets);
            return true;
        } else if (outputFilenameEmptyInputSheetsTextField.getText().isEmpty()){
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter an output filename");
            return false;
        } else if (!outputFilenameEmptyInputSheetsTextField.getText().endsWith(".xlsx")) {
            errorOccurred(statusLabelEmptyInputSheets, "Error: Output filename must end in .xlsx");
            return false;
        } else {
            clearError(statusLabelEmptyInputSheets);
            return true;
        }
    }

    private boolean checkNumReplicatesEmptyInputSheet() {
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty() && !failedEmptyNumReplicatesEmptyInputSheet) {
            clearError(statusLabelEmptyInputSheets);
            return true;
        }
        else if (numReplicatesEmptyInputSheetTextField.getText().isEmpty()) {
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please enter a number of replicates");
            return false;
        }
        try {
            long numReplicates = Long.parseLong(numReplicatesEmptyInputSheetTextField.getText());
            if (numReplicates < 1){
                errorOccurred(statusLabelEmptyInputSheets, "Error: Number must be > 0");
                return false;
            }
            else if (numReplicates > 127) {
                errorOccurred(statusLabelEmptyInputSheets, "Error: Number must be <= 127");
                return false;
            }
            else {
                clearError(statusLabelEmptyInputSheets);
                return true;
            }
        } catch (NumberFormatException e) {
            errorOccurred(statusLabelEmptyInputSheets, "Error: Invalid number: \"" + numReplicatesEmptyInputSheetTextField.getText() + "\"");
            return false;
        }
    }

    private boolean checkSampleLabelingRadioButtons() {
        if (!conditionLabelingRadioButton.isSelected() && !strainLabelingRadioButton.isSelected() && !conditionAndStrainLabelingRadioButton.isSelected()) {
            errorOccurred(statusLabelEmptyInputSheets, "Error: Please select a sample labeling method");
            return false;
        }
        return true;
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
        if (numReplicatesEmptyInputSheetTextField.getText().isEmpty() && !failedEmptyNumReplicatesEmptyInputSheet) clearError(statusLabelEmptyInputSheets);
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
            clearError(statusLabelEmptyInputSheets);
        if (keyEvent == null
                || !(numTimepointsTextField.getScene().focusOwnerProperty().get().getId().equals("numTimepointsTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkNumTimepointsEmptyInputSheet();
        }
    }

    public void handleIncludeBaselineColumn() {
        emptyInputValidateFields();
        config.setIncludeBaselineColumn(includeBaselineColumnCheckbox.isSelected()); }
}
