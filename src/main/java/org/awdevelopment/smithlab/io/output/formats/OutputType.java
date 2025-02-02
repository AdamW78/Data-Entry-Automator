package org.awdevelopment.smithlab.io.output.formats;

public enum OutputType {
    PRISM, STATISTICAL_TESTS, RAW, BOTH;

    @Override
    public String toString() { return formatString(this.name()); }

    public static String formatString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (chars[i] == '_') {
                chars[i] = ' ';
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
