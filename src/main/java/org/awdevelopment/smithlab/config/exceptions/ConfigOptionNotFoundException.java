package org.awdevelopment.smithlab.config.exceptions;

import org.awdevelopment.smithlab.config.AbstractConfig;
import org.awdevelopment.smithlab.config.ConfigOption;

public class ConfigOptionNotFoundException extends ConfigException {
    public ConfigOptionNotFoundException(ConfigOption option, AbstractConfig config) {
        super("Config option \"" + option + "\" not found in config \"" + config + "\".");
    }
}
