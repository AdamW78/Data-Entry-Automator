package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.logging.LoggerHelper;

public abstract class AbstractController {

    private LoggerHelper LOGGER;

    public AbstractController() {}

    public abstract void setup();

    public void setLogger(LoggerHelper LOGGER) {
        this.LOGGER = LOGGER;
    }

    public LoggerHelper getLogger() {
        return LOGGER;
    }
}
