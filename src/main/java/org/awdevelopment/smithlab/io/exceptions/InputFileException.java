package org.awdevelopment.smithlab.io.exceptions;

import java.io.IOException;

public abstract class InputFileException extends IOException {
    protected InputFileException(String message) {
        super(message);
    }
}
