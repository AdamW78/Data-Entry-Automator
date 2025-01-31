package org.awdevelopment.smithlab.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class GenerateOutputSheetsController extends AbstractController {

    @FXML
    private RadioButton outputStylePrismRadioButton;
    @FXML
    private TextField inputFileTextField;
    @FXML
    private Button inputFileBrowseButton;
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
    private ChoiceBox sampleSortingMethodChoiceBox;

    public void initialize() {

    }

    public void handleRadioButtonPress(ActionEvent actionEvent) {
    }

    public void generateOutput(ActionEvent actionEvent) {
    }

    public void updateOutputFilename(KeyEvent keyEvent) {
    }

    public void updateSampleSortingMethod(ActionEvent actionEvent) {
    }

    public void updateInputFile(KeyEvent keyEvent) {
    }

    public void browseForInputFile(ActionEvent actionEvent) {
    }
}
