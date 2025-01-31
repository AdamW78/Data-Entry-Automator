package org.awdevelopment.smithlab.gui.controllers;

import org.apache.logging.log4j.Logger;

public class AbstractController {

    private Logger LOGGER;

    public AbstractController() {}

    public void setLogger(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    public Logger getLogger() {
        return LOGGER;
    }
}
