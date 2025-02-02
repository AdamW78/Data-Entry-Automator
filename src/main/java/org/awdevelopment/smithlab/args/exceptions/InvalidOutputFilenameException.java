package org.awdevelopment.smithlab.args.exceptions;

public class InvalidOutputFilenameException extends IllegalArgumentException {
    public InvalidOutputFilenameException(String filename) {
        super("Error: Invalid output filename: \"" + filename + "\"");
    }
}
