package org.awdevelopment.smithlab.args.exceptions;

import java.io.FileNotFoundException;

public class InputFileNotFoundException extends FileNotFoundException {
    public InputFileNotFoundException(String filepath) {
      super("Error: Input file not found: " + filepath);
    }
}
