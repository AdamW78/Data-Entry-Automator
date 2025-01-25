package org.awdevelopment.smithlab.args.exceptions;

public class FileNameException extends IllegalArgumentException {

    public enum FileNameExceptionType {
        EMPTY,
        INVALID_DASH
    }

    private FileNameException(String message) {
        super(message);
    }

    public FileNameException(String fileName, FileNameExceptionType type) {
        switch (type) {
            case EMPTY -> throw new FileNameException("Error: Invalid file name: \"" + fileName + "\" - File name cannot be empty.");
            case INVALID_DASH -> throw new FileNameException("Error: Invalid filename \"" + fileName + "\"File name cannot contain a dash.");
        }
    }
}
