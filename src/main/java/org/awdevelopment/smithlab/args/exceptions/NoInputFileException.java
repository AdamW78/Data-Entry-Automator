package org.awdevelopment.smithlab.args.exceptions;

import java.util.Arrays;

public class NoInputFileException extends IllegalArgumentException {
    public NoInputFileException(String[] args) {
        super("No input file provided. Provide one with \"-i <input file path>\" " +
                "or \"--input <input file path>\" with the  Arguments: " + Arrays.stream(args).reduce("", (a, b) -> a + " " + b));

    }
}
