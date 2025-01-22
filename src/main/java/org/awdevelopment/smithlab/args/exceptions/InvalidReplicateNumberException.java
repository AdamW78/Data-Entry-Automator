package org.awdevelopment.smithlab.args.exceptions;

public class InvalidReplicateNumberException extends IllegalArgumentException{

    private InvalidReplicateNumberException(String message) {
        super(message);
    }

    public InvalidReplicateNumberException(InvalidReplicateNumberReason reason) {
        switch (reason) {
            case LESS_THAN_ZERO:
                throw new InvalidReplicateNumberException("The number of replicates must be greater than zero.");
            case NOT_AN_INTEGER:
                throw new InvalidReplicateNumberException("The number of replicates must be an integer.");
            case GREATER_THAN_100:
                throw new InvalidReplicateNumberException("The number of replicates must be less than or equal to 100.");
        }

    }

    public enum InvalidReplicateNumberReason {
        LESS_THAN_ZERO, NOT_AN_INTEGER, GREATER_THAN_100
    }
}
