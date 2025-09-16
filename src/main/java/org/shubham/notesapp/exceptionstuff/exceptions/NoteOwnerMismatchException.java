package org.shubham.notesapp.exceptionstuff.exceptions;

public class NoteOwnerMismatchException extends RuntimeException {
    public NoteOwnerMismatchException(String message){
        super(message);
    }
}
