package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;

public class AbstractLabelController extends AbstractController {

    private EmptyInputSheetConfig config;

    public AbstractLabelController() { super(); }

    public void setConfig(EmptyInputSheetConfig config) {
        this.config = config;
    }

    public EmptyInputSheetConfig config() {
        return config;
    }
}
