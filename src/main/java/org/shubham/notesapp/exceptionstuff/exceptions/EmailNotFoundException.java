package org.shubham.notesapp.exceptionstuff.exceptions;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException(String message) {
    super(message);
  }
}
