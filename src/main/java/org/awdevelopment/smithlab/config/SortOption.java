package org.awdevelopment.smithlab.config;

public enum SortOption {
    NONE,
    ALPHABETICAL,
    REVERSE_ALPHABETICAL,
    SAMPLE_NUMBER;

    private static String[] getDisplayNames() {
        return new String[] { "None", "Alphabetical", "Reverse Alphabetical", "Sample Number" };
    }

    @Override
    public String toString() {
        return getDisplayNames()[ordinal()];
    }
}
