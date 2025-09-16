package org.shubham.notesapp.exceptionstuff.exceptions;

public class UserIsNullException extends RuntimeException {
    public UserIsNullException(String message) {
        super(message);
    }
}
