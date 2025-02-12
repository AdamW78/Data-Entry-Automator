package org.awdevelopment.smithlab.gui.controllers.main;

import org.awdevelopment.smithlab.config.Mode;

public class IllegalFieldAccessException extends IllegalAccessException {
    public IllegalFieldAccessException(Mode mode, String id) {
        super("Field with id: \"" + id + "\" is not accessible in mode: " + mode);
    }
}
