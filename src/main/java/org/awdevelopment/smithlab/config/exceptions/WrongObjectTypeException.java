package org.awdevelopment.smithlab.config.exceptions;

import org.awdevelopment.smithlab.config.ConfigEntry;

public class WrongObjectTypeException extends ConfigException {
    public <T> WrongObjectTypeException(ConfigEntry<?> entry, T value) {
        super("Wrong object type for " + entry.option().name() + ": Needed type \"" + entry.value().getClass().getName()
                + "\" but provided type was: \"" + value.getClass().getName() + "\".");
    }
}
