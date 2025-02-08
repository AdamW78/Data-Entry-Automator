package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;

public class AbstractLabelController extends AbstractController {

    private Config config;

    public AbstractLabelController() { super(); }

    public void setConfig(EmptyInputSheetConfig config) {
        this.config = config;
    }

    public Config config() {
        return config;
    }
}
