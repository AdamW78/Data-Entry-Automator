package org.awdevelopment.smithlab.args.exceptions;

public class NoSuchArgumentException extends IllegalArgumentException {
    public NoSuchArgumentException(String unknownArgument) {
        super("Error: Unknown argument: " + unknownArgument);
    }
}
