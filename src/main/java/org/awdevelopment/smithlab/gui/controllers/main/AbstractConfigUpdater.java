package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.input.KeyEvent;
import org.awdevelopment.smithlab.config.ConfigOption;

public abstract class AbstractConfigUpdater {

    public abstract void updateFields();

    public abstract void updateSampleSortingMethod();

    abstract void updateTextField(ValidatableField field, ConfigOption option, KeyEvent keyEvent);
}
