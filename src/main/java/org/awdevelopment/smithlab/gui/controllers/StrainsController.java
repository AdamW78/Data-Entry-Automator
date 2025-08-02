package org.awdevelopment.smithlab.gui.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.awdevelopment.smithlab.data.Strain;

import java.util.*;

/**
 * Controller that maintains a dynamic list of TextFields so that:
 *  - There is always exactly one empty field at the bottom.
 *  - When the bottom field becomes non-empty, a new empty field is appended.
 *  - When a user clears a field and leaves it, it disappears if another empty bottom field exists.
 */
public class StrainsController extends AbstractLabelController {

    @FXML private VBox strainsBox;
    @FXML private Button saveCloseButton;

    private boolean usingNumStrains;

    private final Set<Strain> strains = new LinkedHashSet<>();
    private final Map<TextField, Strain> textFieldToStrain = new HashMap<>();

    // ----------------- lifecycle -----------------

    @FXML
    public void initialize() {
        // Make sure we start with one empty field at the bottom
        ensureOneBlankAtBottom();
        this.usingNumStrains = true;
    }

    @Override
    public void setup() { }

    // ----------------- public API -----------------

    public boolean usingNumStrains() {
        return usingNumStrains;
    }

    public void setUsingNumStrains(boolean usingNumStrains) {
        this.usingNumStrains = usingNumStrains;
    }

    public Set<Strain> getStrains() {
        return Collections.unmodifiableSet(strains);
    }

    /**
     * Pre-populate the UI with known strains. You may call this before the view is shown.
     */
    public void loadStrains(Collection<Strain> initialStrains) {
        strains.clear();
        strains.addAll(initialStrains);

        strainsBox.getChildren().clear();
        textFieldToStrain.clear();

        for (Strain s : initialStrains) {
            TextField tf = createField(s.getName());
            strainsBox.getChildren().add(tf);
            textFieldToStrain.put(tf, s);
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
            tf.setPromptText("Enter strain name...");
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
            Strain strain = textFieldToStrain.remove(tf);
            if (strain != null) {
                strains.remove(strain);
                super.config().setStrains(strains);
                if (strains.isEmpty()) {
                    this.setUsingNumStrains(true);
                    this.getMainController().enableNumStrains();
                }
            }
        } else {
            Strain strain = textFieldToStrain.get(tf);
            if (strain == null) {
                strain = new Strain(trimmed);
                textFieldToStrain.put(tf, strain);
                strains.add(strain);
                super.config().setStrains(strains);
                super.getMainController().disableNumStrains();
                this.setUsingNumStrains(false);
            } else if (!trimmed.equals(strain.getName())) {
                strain.setName(trimmed);
            }

            if (isLast(tf)) {
                strainsBox.getChildren().add(createField(""));
            }
        }
    }

    private void onFocusLost(TextField tf) {
        if (!tf.getText().trim().isEmpty()) return;

        if (strainsBox.getChildren().size() > 1) {
            TextField last = lastField();
            if (last != tf && last.getText().trim().isEmpty()) {
                strainsBox.getChildren().remove(tf);
            }
        }

        ensureOneBlankAtBottom();
    }

    private void ensureOneBlankAtBottom() {
        if (strainsBox.getChildren().isEmpty()) {
            strainsBox.getChildren().add(createField(""));
            return;
        }

        TextField last = lastField();
        if (!last.getText().trim().isEmpty()) {
            strainsBox.getChildren().add(createField(""));
        } else {
            pruneExtraEmptiesKeepOne();
        }
    }

    private void pruneExtraEmptiesKeepOne() {
        List<TextField> empties = new ArrayList<>();
        for (Node n : strainsBox.getChildren()) {
            if (n instanceof TextField tf && tf.getText().trim().isEmpty()) {
                empties.add(tf);
            }
        }
        for (int i = 0; i < empties.size() - 1; i++) {
            strainsBox.getChildren().remove(empties.get(i));
        }
    }

    private boolean isLast(TextField tf) {
        ObservableList<Node> kids = strainsBox.getChildren();
        return !kids.isEmpty() && kids.getLast() == tf;
    }

    private TextField lastField() {
        ObservableList<Node> kids = strainsBox.getChildren();
        return (TextField) kids.getLast();
    }
}
