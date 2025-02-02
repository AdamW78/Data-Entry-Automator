package org.awdevelopment.smithlab.io.exceptions;

import java.io.IOException;

public abstract class InvalidTimepointException extends IOException {
  protected InvalidTimepointException(String message) {super(message);}
  @Override
    public String toString() {return getDisplayName();}
  public abstract String getDisplayName();
}
