package org.awdevelopment.smithlab.config.exceptions;

public abstract class ConfigException extends Exception {
    protected ConfigException(String message) {
        super(message);
    }
}
