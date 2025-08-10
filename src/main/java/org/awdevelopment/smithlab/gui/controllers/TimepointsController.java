package org.awdevelopment.smithlab.gui.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

/**
 * Controller that manages a dynamic list of TextFields that represent
 * positive byte values (1–127), enforcing validation and updating internal state.
 */
public class TimepointsController extends AbstractLabelController {

    @FXML private VBox daysBox;
    @FXML private Button saveCloseButton;

    private boolean usingNumDays;

    private final Set<Byte> days = new LinkedHashSet<>();
    private final Map<TextField, Byte> textFieldToByte = new HashMap<>();

    // ----------------- lifecycle -----------------

    @FXML
    public void initialize() {
        ensureOneBlankAtBottom();
        this.usingNumDays = true;
    }

    @Override
    public void setup() { }

    // ----------------- public API -----------------

    public boolean usingNumDays() {
        return usingNumDays;
    }

    public void setUsingNumDays(boolean usingNumDays) {
        this.usingNumDays = usingNumDays;
    }

    public Set<Byte> getDays() {
        return Collections.unmodifiableSet(days);
    }

    public void loadDays(Collection<Byte> initialDays) {
        days.clear();
        days.addAll(initialDays);

        daysBox.getChildren().clear();
        textFieldToByte.clear();

        for (Byte b : initialDays) {
            TextField tf = createField(Byte.toString(b));
            daysBox.getChildren().add(tf);
            textFieldToByte.put(tf, b);
        }

        ensureOneBlankAtBottom();
    }

    // ----------------- UI callbacks -----------------

    @FXML
    private void handleSaveAndClose(ActionEvent ignoredEvent) {
        pruneExtraEmptiesKeepOne();
        Stage stage = (Stage) saveCloseButton.getScene().getWindow();
        stage.close();
    }

    // ----------------- core logic -----------------

    private TextField createField(String text) {
        TextField tf = new TextField(text);

        if (text == null || text.isEmpty()) {
            tf.setPromptText("Enter day (1–127)");
        }

        tf.textProperty().addListener((obs, oldValue, newValue) -> onTextChanged(tf, newValue));
        tf.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                onFocusLost(tf);
            }
        });

        return tf;
    }

    private void onTextChanged(TextField tf, String newValue) {
        String trimmed = newValue.trim();

        if (trimmed.isEmpty()) {
            Byte oldValue = textFieldToByte.remove(tf);
            if (oldValue != null) {
                days.remove(oldValue);
                super.config().setDays(days);
                if (days.isEmpty()) {
                    setUsingNumDays(true);
                    getMainController().enableNumDays();
                }
            }
            tf.setStyle(""); // Clear styling
            return;
        }

        // Try to parse the input as a positive byte (1–127)
        try {
            byte parsed = Byte.parseByte(trimmed);
            if (parsed <= 0) throw new NumberFormatException();

            // Check if this field already maps to a different byte
            Byte existing = textFieldToByte.get(tf);
            if (existing != null && !existing.equals(parsed)) {
                days.remove(existing);
            }

            textFieldToByte.put(tf, parsed);
            days.add(parsed);
            super.config().setDays(days);
            super.getMainController().disableNumDays();
            setUsingNumDays(false);

            tf.setStyle(""); // Clear error styling

            if (isLast(tf)) {
                daysBox.getChildren().add(createField(""));
            }

        } catch (NumberFormatException e) {
            // Invalid: not a number or not in byte range
            tf.setStyle("-fx-border-color: red; -fx-border-width: 1;");
            textFieldToByte.remove(tf);
            Byte existing = textFieldToByte.get(tf);
            if (existing != null) {
                days.remove(existing);
            }

        }
    }

    private void onFocusLost(TextField tf) {
        if (!tf.getText().trim().isEmpty()) return;

        if (daysBox.getChildren().size() > 1) {
            TextField last = lastField();
            if (last != tf && last.getText().trim().isEmpty()) {
                daysBox.getChildren().remove(tf);
            }
        }

        ensureOneBlankAtBottom();
    }

    private void ensureOneBlankAtBottom() {
        if (daysBox.getChildren().isEmpty()) {
            daysBox.getChildren().add(createField(""));
            return;
        }

        TextField last = lastField();
        if (!last.getText().trim().isEmpty()) {
            daysBox.getChildren().add(createField(""));
        } else {
            pruneExtraEmptiesKeepOne();
        }
    }

    private void pruneExtraEmptiesKeepOne() {
        List<TextField> empties = new ArrayList<>();
        for (Node n : daysBox.getChildren()) {
            if (n instanceof TextField tf && tf.getText().trim().isEmpty()) {
                empties.add(tf);
            }
        }
        for (int i = 0; i < empties.size() - 1; i++) {
            daysBox.getChildren().remove(empties.get(i));
        }
    }

    private boolean isLast(TextField tf) {
        ObservableList<Node> kids = daysBox.getChildren();
        return !kids.isEmpty() && kids.getLast() == tf;
    }

    private TextField lastField() {
        ObservableList<Node> kids = daysBox.getChildren();
        return (TextField) kids.getLast();
    }
}
