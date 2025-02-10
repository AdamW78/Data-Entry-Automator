package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.ConfigOption;

public abstract class AbstractConfigUpdater {

    public abstract void updateFields();

    public abstract void updateSampleSortingMethod();

    abstract void updateTextField(TextField textField, ConfigOption option, KeyEvent keyEvent, Label errorLabel, boolean failedBoolean, boolean byteParse, boolean fileExists);
}
