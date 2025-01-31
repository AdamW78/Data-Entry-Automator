package org.awdevelopment.smithlab.gui;

import javafx.scene.control.RadioButton;

public class RadioButtonGroup {

    public RadioButtonGroup(RadioButton... radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnAction(event -> {
                for (RadioButton otherRadioButton : radioButtons) {
                    if (!otherRadioButton.equals(radioButton)) {
                        otherRadioButton.setSelected(false);
                    }
                }
            });
        }
    }
}
