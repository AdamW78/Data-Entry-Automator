package org.awdevelopment.smithlab.config;

public class ConfigEntry<V> {

    private final ConfigOption option;
    private V value;

    public ConfigEntry(ConfigOption option, V value) {
        this.option = option;
        this.value = value;
    }

    public ConfigOption option() {
        return option;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public V value() {
        return value;
    }
}
