package org.awdevelopment.smithlab.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.gui.RadioButtonGroup;
import org.awdevelopment.smithlab.io.input.InputReader;
import org.awdevelopment.smithlab.io.output.OutputGenerator;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class MainApplicationController extends AbstractController {

    private boolean failedEmptyFile = false;
    private Config config;
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

    public void initialize() {
        config = new Config();
        new RadioButtonGroup(
                outputStylePrismRadioButton,
                outputStyleTestsRadioButton,
                outputStyleRawRadioButton,
                outputStyleBothRadioButton
        );
        sampleSortingMethodChoiceBox.getItems().addAll(SortOption.values());
        sampleSortingMethodChoiceBox.setValue(SortOption.NONE);
    }

    public void handleRadioButtonPress() {
        if (outputStylePrismRadioButton.isSelected()) {
            config.setOutputType(OutputType.PRISM);
        } else if (outputStyleTestsRadioButton.isSelected()) {
            config.setOutputType(OutputType.OTHER);
        } else if (outputStyleRawRadioButton.isSelected()) {
            config.setOutputType(OutputType.RAW);
        } else if (outputStyleBothRadioButton.isSelected()) {
            config.setOutputType(OutputType.BOTH);
        }
    }

    public void generateOutput() {
        if (notReadyForOutput()) return;
        if (config.mode() == Mode.GENERATE_OUTPUT_SHEETS) {
            InputReader reader = new InputReader(config, getLogger());
            Experiment experiment = reader.readExperimentData();
            OutputGenerator outputGenerator = new OutputGenerator(config, getLogger());
            outputGenerator.generateOutput(experiment);
        } else if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) {
            OutputGenerator outputGenerator = new OutputGenerator(config, getLogger());
            outputGenerator.generateEmptyInputSheet();
        }
    }

    private boolean notReadyForOutput() {
        checkInputFile();
        if (inputFileExistsLabel.getStyle().equals("-fx-text-fill: red")) return true;
        if (inputFileExistsLabel.getText().isEmpty()) {
            failedEmptyFile = true;
            inputFileExistsLabel.setText("Error: Please enter a valid input file path or browse for a file");
            inputFileExistsLabel.setStyle("-fx-text-fill: red");
            return true;
        }
        return false;
    }

    public void updateOutputFilename() {
        config.setOutputFilename(outputFileTextField.getText());
    }

    public void updateSampleSortingMethod() {
        config.setSortOption(sampleSortingMethodChoiceBox.getValue());
    }

    public void updateInputFile(KeyEvent keyEvent) {
        File inputFile = new File(inputFileTextField.getText());
        config.setInputFile(inputFile);
        Scene scene = inputFileTextField.getScene();
        if (    keyEvent == null
                || !(scene.focusOwnerProperty().get().getId().equals("inputFileTextField"))
                || keyEvent.getCode() == KeyCode.ENTER
                || keyEvent.getCode() == KeyCode.TAB
        ) {
            checkInputFile();
        }
    }

    public void browseForInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input File (ending in .xlsx)");
        File inputFile = fileChooser.showOpenDialog(inputFileBrowseButton.getScene().getWindow());
        if (inputFile != null) {
            inputFileTextField.setText(inputFile.getAbsolutePath());
            updateInputFile(null);
        }
    }

    public void updateTextFields() { updateInputFile(null); }

    private void checkInputFile() {
        if (inputFileTextField.getText().isEmpty() && !failedEmptyFile) {
            inputFileExistsLabel.setText("");
            inputFileExistsLabel.setStyle("");
            return;
        }
        File inputFile = new File(inputFileTextField.getText());
        if (inputFile.exists()) {
            if (inputFile.isDirectory()) {
                inputFileExistsLabel.setText("Error: Input file \"" + inputFileTextField.getText() + "\" is a directory");
                inputFileExistsLabel.setStyle("-fx-text-fill: red");
            } else {
                inputFileExistsLabel.setText("Input file \"" + inputFileTextField.getText() + "\" exists!");
                inputFileExistsLabel.setStyle("-fx-text-fill: green");
            }
        } else {
            inputFileExistsLabel.setText("Error: Input file \"" + inputFileTextField.getText() + "\" does not exist");
            inputFileExistsLabel.setStyle("-fx-text-fill: red");
        }
    }

    public void handleAddSheetsCheckbox() {
        if (addSheetsToInputFileCheckbox.isSelected()) {
            outputFileTextField.setDisable(true);
            config.setWriteToDifferentFile(false);
            config.setOutputFilename(inputFileTextField.getText());
        } else {
            outputFileTextField.setDisable(false);
            config.setWriteToDifferentFile(true);
            config.setOutputFilename(outputFileTextField.getText());
        }
    }

    public void setMode() {
        if (tabPane.getSelectionModel().getSelectedItem() == outputSheetsTab) {
            config.setMode(Mode.GENERATE_OUTPUT_SHEETS);
        } else if (tabPane.getSelectionModel().getSelectedItem() == emptyInputSheetTab) {
            config.setMode(Mode.GENERATE_EMPTY_INPUT_SHEET);
        }
    }
}
