package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.config.exceptions.ConfigException;
import org.awdevelopment.smithlab.config.exceptions.ConfigOptionNotFoundException;
import org.awdevelopment.smithlab.config.exceptions.WrongObjectTypeException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractConfig {

    private final Set<ConfigEntry<?>> config;
    private final LoggerHelper LOGGER;

    protected AbstractConfig(LoggerHelper logger) {
        this.config = new HashSet<>();
        this.LOGGER = logger;
        config.add(new ConfigEntry<>(ConfigOption.GUI, ConfigDefaults.GUI));
    }

    public <T> T get(ConfigOption option) {
        try {
            return getConfigEntryValue(option);
        } catch (ConfigException e) {
            LOGGER().atFatal(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public <T> void set(ConfigOption option, T value) {
        try {
            setConfigEntry(option, value);
        } catch (ConfigException e) {
            LOGGER().atFatal(e.getMessage());
            System.exit(1);
        }
    }

    protected <T> void addConfigEntry(ConfigEntry<T> entry) {
        config.add(entry);
    }

    private Optional<ConfigEntry<?>> getConfigEntry(ConfigOption option) {
        return config.stream().filter(e -> e.option().equals(option)).findFirst();
    }

    private <T> T getConfigEntryValue(ConfigOption option) throws ConfigException {
        Optional<ConfigEntry<?>> entryOptional = getConfigEntry(option);
        if (entryOptional.isPresent()) {
            ConfigEntry<T> typedEntry;
            try {
                typedEntry = (ConfigEntry<T>) entryOptional.get();
            } catch (ClassCastException e) {
                throw new WrongObjectTypeException(entryOptional.get(), this);
            }
            return typedEntry.value();
        } else {
            throw new ConfigOptionNotFoundException(option, this);
        }
    }


    private <T> void setConfigEntry(ConfigOption option, T value) throws ConfigException {
        Optional<ConfigEntry<?>> entryOptional = getConfigEntry(option);
        if (entryOptional.isPresent()) {
            ConfigEntry<T> typedEntry;
            try { typedEntry = (ConfigEntry<T>) entryOptional.get(); }
            catch (ClassCastException e) { throw new WrongObjectTypeException(entryOptional.get(), this); }
            typedEntry.setValue(value);
        } else throw new ConfigOptionNotFoundException(option, this);
    }

    public boolean GUI() { return get(ConfigOption.GUI); }
    public void setGUI(boolean GUI) { set(ConfigOption.GUI, GUI); }

    @Override
    public abstract String toString();

    public abstract Mode mode();

    public LoggerHelper LOGGER() { return LOGGER; }
}
