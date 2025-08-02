package org.awdevelopment.smithlab.gui.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.awdevelopment.smithlab.data.Condition;

import java.util.*;

/**
 * Controller that maintains a dynamic list of TextFields so that:
 *  - There is always exactly one empty field at the bottom.
 *  - When the bottom field becomes non-empty, a new empty field is appended.
 *  - When a user clears a field and leaves it, it disappears if another empty bottom field exists.
 */
public class ConditionsController extends AbstractLabelController {

    @FXML private VBox conditionsBox;
    @FXML private Button saveCloseButton;

    private boolean usingNumConditions;

    private final Set<Condition> conditions = new LinkedHashSet<>();
    private final Map<TextField, Condition> textFieldToCondition = new HashMap<>();

    // ----------------- lifecycle -----------------

    @FXML
    public void initialize() {
        // Make sure we start with one empty field at the bottom
        ensureOneBlankAtBottom();
        this.usingNumConditions = true;
    }

    @Override
    public void setup() {  }

    // ----------------- public API -----------------

    public boolean usingNumConditions() {
        return usingNumConditions;
    }

    public void setUsingNumConditions(boolean usingNumConditions) {
        this.usingNumConditions = usingNumConditions;
    }

    public Set<Condition> getConditions() {
        return Collections.unmodifiableSet(conditions);
    }

    /**
     * Pre-populate the UI with known conditions. You may call this before the view is shown.
     */
    public void loadConditions(Collection<Condition> initialConditions) {
        conditions.clear();
        conditions.addAll(initialConditions);

        conditionsBox.getChildren().clear();
        textFieldToCondition.clear();

        for (Condition c : initialConditions) {
            TextField tf = createField(c.getName());
            conditionsBox.getChildren().add(tf);
            textFieldToCondition.put(tf, c);
        }
        ensureOneBlankAtBottom();
    }

    // ----------------- UI callbacks -----------------

    @FXML
    private void handleSaveAndClose(ActionEvent ignoredEvent) {
        // Optionally, clean trailing empties (leaving exactly one at bottom):
        pruneExtraEmptiesKeepOne();
        Stage stage = (Stage) saveCloseButton.getScene().getWindow();
        stage.close();
    }

    // ----------------- core logic -----------------

    private TextField createField(String text) {

        TextField tf = new TextField(text);

        // Show placeholder only if the field is empty
        if (text == null || text.isEmpty()) {
            tf.setPromptText("Enter condition name...");
        }

        // React live to changes
        tf.textProperty().addListener((obs, oldValue, newValue) -> onTextChanged(tf, newValue));

        // On focus loss, maybe remove if empty
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
            // Remove mapping and condition if it existed
            Condition cond = textFieldToCondition.remove(tf);
            if (cond != null) {
                conditions.remove(cond);
                super.config().setConditions(conditions);
                if (conditions.isEmpty()) {
                    this.setUsingNumConditions(true);
                    this.getMainController().enableNumConditions();
                }
            }
        } else {
            // Non-empty: make sure a Condition exists and stays updated
            Condition cond = textFieldToCondition.get(tf);
            if (cond == null) {
                cond = new Condition(trimmed);
                textFieldToCondition.put(tf, cond);
                conditions.add(cond);
                super.config().setConditions(conditions);
                super.getMainController().disableNumConditions();
                this.setUsingNumConditions(false);
            } else if (!trimmed.equals(cond.getName())) {
                cond.setName(trimmed);
            }

            // If user typed into the last (blank) field, append a new blank
            if (isLast(tf)) {
                conditionsBox.getChildren().add(createField(""));
            }
        }
    }

    private void onFocusLost(TextField tf) {
        if (!tf.getText().trim().isEmpty()) {
            return; // only care about empty fields
        }

        // If it's not the only field AND there's another empty field at the bottom, remove it
        if (conditionsBox.getChildren().size() > 1) {
            TextField last = lastField();
            if (last != tf && last.getText().trim().isEmpty()) {
                conditionsBox.getChildren().remove(tf);
            }
        }

        // Guarantee there is one (and exactly one) empty field at the bottom
        ensureOneBlankAtBottom();
    }

    private void ensureOneBlankAtBottom() {
        if (conditionsBox.getChildren().isEmpty()) {
            conditionsBox.getChildren().add(createField(""));
            return;
        }

        TextField last = lastField();
        if (!last.getText().trim().isEmpty()) {
            conditionsBox.getChildren().add(createField(""));
        } else {
            // Also ensure there aren't multiple blank fields at bottom accidentally
            pruneExtraEmptiesKeepOne();
        }
    }

    private void pruneExtraEmptiesKeepOne() {
        List<TextField> empties = new ArrayList<>();
        for (Node n : conditionsBox.getChildren()) {
            if (n instanceof TextField tf && tf.getText().trim().isEmpty()) {
                empties.add(tf);
            }
        }
        // keep the *last* one, remove all previous empties
        for (int i = 0; i < empties.size() - 1; i++) {
            conditionsBox.getChildren().remove(empties.get(i));
        }
    }

    private boolean isLast(TextField tf) {
        ObservableList<Node> kids = conditionsBox.getChildren();
        return !kids.isEmpty() && kids.getLast() == tf;
    }

    private TextField lastField() {
        ObservableList<Node> kids = conditionsBox.getChildren();
        return (TextField) kids.getLast();
    }

}
