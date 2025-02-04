package org.awdevelopment.smithlab.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.io.exceptions.InputFileException;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class MainApplicationController extends AbstractController {

    private final Config config;
    @FXML
    private Label successFailureLabel;
    @FXML
    private Label replicatesErrorLabel;
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

    private boolean failedEmptyReplicates = false;
    private boolean failedEmptyOutputFilename = false;
    private boolean failedEmptyInputSheetName = false;
    private boolean failedEmptyInputFile = false;

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
        sampleSortingMethodChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodChoiceBox.setValue(config.sortOption());
        setupErrorLabels();
        updateFields();
    }

    private void setupErrorLabels() {
        replicatesErrorLabel.setText("");
        outputStyleErrorLabel.setText("");
        outputFilenameErrorLabel.setText("");
        inputFileExistsLabel.setText("");
        successFailureLabel.setText("");
        replicatesErrorLabel.setStyle("");
        outputStyleErrorLabel.setStyle("");
        outputFilenameErrorLabel.setStyle("");
        inputFileExistsLabel.setStyle("");
        successFailureLabel.setStyle("");
    }

    public void generateOutput() {
        if (notReadyForOutput()) return;
        if (config.mode() == Mode.GENERATE_OUTPUT_SHEETS) if (!generateOutputSheets()) return;
        else if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) if (!generateEmptyInputSheet()) return;
        setupErrorLabels();
        successFailureLabel.setText("Successfully generated output!");
        successFailureLabel.setStyle("-fx-text-fill: green");
    }

    private boolean generateEmptyInputSheet() {
        OutputGenerator outputGenerator = new OutputGenerator(config, getLogger());
        try {
            outputGenerator.generateEmptyInputSheet();
        } catch (OutputException e) {
            errorOccurred(successFailureLabel, e.getMessage());
            return false;
        }
        return true;
    }

    private boolean generateOutputSheets() {
        getLogger().atDebug("FROM GUI: USER CLICKED GENERATE BUTTON");
        getLogger().atInfo("Generating output...");
        getLogger().atDebug(new String[] {
                "Input file: " + config.inputFile().getPath(),
                "Output file: " + config.outputFilename(),
                "Output type: " + config.outputType(),
                "Write to different file: " + config.writeToDifferentFile(),
                "Sort option: " + config.sortOption(),
                "Number of replicates: " + config.numberOfReplicates(),
                "Empty input sheet name: " + config.emptyInputSheetName()
        });
        InputReader reader = new InputReader(config, getLogger());
        getLogger().atDebug("Successfully initialized InputReader -  reading experiment data...");
        Experiment experiment;
        try {
            experiment = reader.readExperimentData();
        } catch (InputFileException e) {
            errorOccurred(successFailureLabel, e.getMessage());
            return false;
        }
        getLogger().atDebug("Successfully read experiment data - Initializing OutputGenerator...");
        OutputGenerator outputGenerator = new OutputGenerator(config, getLogger());
        getLogger().atDebug("Successfully initialized OutputGenerator - generating output...");
        try {
            outputGenerator.generateOutput(experiment);
        } catch (OutputException e) {
            errorOccurred(successFailureLabel, e.getMessage());
            return false;
        }
        getLogger().atInfo("Successfully generated output!");
        failedEmptyInputFile = false;
        failedEmptyOutputFilename = false;
        failedEmptyReplicates = false;
        return true;
    }

    private boolean notReadyForOutput() {
        if (sampleSortingMethodChoiceBox.getValue() == null) config.setSortOption(SortOption.NONE);
        boolean badInputFile = badInputFileField();
        boolean badOutputFilename = badOutputFilenameTextField();
        boolean badNumReplicates = badNumReplicatesTextField();
        boolean badOutputStyle = badOutputStyleRadioButton();
        return badInputFile || badOutputFilename || badNumReplicates || badOutputStyle;
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
        if (!checkOutputFilename()) return true;
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
                errorOccurred(replicatesErrorLabel, "Error: Please enter a number of replicates");
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
            clearError(replicatesErrorLabel);
            return true;
        }
        else if (numReplicatesTextField.getText().isEmpty() && (config.outputType() == OutputType.STATISTICAL_TESTS || config.outputType() == OutputType.BOTH)) {
            errorOccurred(replicatesErrorLabel, "Error: Please enter a number of replicates");
            return false;
        }
        try {
            short numReplicates = Short.parseShort(numReplicatesTextField.getText());
            if (numReplicates < 1) {
                errorOccurred(replicatesErrorLabel, "Error: Number must be > 0");
                return false;
            } else {
                clearError(replicatesErrorLabel);
                return true;
            }
        } catch (NumberFormatException e) {
            errorOccurred(replicatesErrorLabel, "Error: Invalid number: \"" + numReplicatesTextField.getText() + "\"");
            return false;
        }
    }

    private boolean checkOutputFilename() {
        if (addSheetsToInputFileCheckbox.isSelected()) {
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(inputFileTextField.getText());
            return true;
        } else if (outputFileTextField.getText().isEmpty() && !failedEmptyOutputFilename) {
            clearError(outputFilenameErrorLabel);
            return true;
        } else if (outputFileTextField.getText().isEmpty()) {
            errorOccurred(outputFilenameErrorLabel, "Error: Please enter an output filename");
            return false;
        } else if (!outputFileTextField.getText().endsWith(".xlsx")) {
            errorOccurred(outputFilenameErrorLabel, "Error: Output filename must end in .xlsx");
            return false;
        } else {
            clearError(outputFilenameErrorLabel);
            return true;
        }
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
        if (numReplicatesTextField.getText().isEmpty() && !failedEmptyReplicates) clearError(replicatesErrorLabel);
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
            checkOutputFilename();
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
}
