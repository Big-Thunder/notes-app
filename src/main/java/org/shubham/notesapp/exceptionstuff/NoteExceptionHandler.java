package org.shubham.notesapp.exceptionstuff;

import org.shubham.notesapp.exceptionstuff.exceptions.NoteNotFoundException;
import org.shubham.notesapp.exceptionstuff.exceptions.NoteOwnerMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class NoteExceptionHandler {
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity handleNoteNotFound(NoteNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoteOwnerMismatchException.class)
    public ResponseEntity handleNoteNotFound(NoteOwnerMismatchException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
