package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.config.Config;

public class AbstractLabelController extends AbstractController {

    private Config config;

    public AbstractLabelController() { super(); }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config config() {
        return config;
    }
}
