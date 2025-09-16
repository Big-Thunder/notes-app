package org.shubham.notesapp.notestuff;

import jakarta.servlet.http.HttpSession;
import org.shubham.notesapp.exceptionstuff.exceptions.NoteNotFoundException;
import org.shubham.notesapp.exceptionstuff.exceptions.NoteOwnerMismatchException;
import org.shubham.notesapp.exceptionstuff.exceptions.UserIsNullException;
import org.shubham.notesapp.loginstuff.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Service
public class NoteService {
    NoteRepo noteRepo;

    NoteService(NoteRepo noteRepo){
        this.noteRepo = noteRepo;
    }

    public Note createNote(String title, String body, User user){
        System.out.println("Creating new note for" + (user.getName()));

        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setBody(body);
        newNote.setUser(user);
        newNote.setCreatedDate(Date.from(Instant.now()));
        newNote.setModifiedDate(Date.from(Instant.now()));

        noteRepo.save(newNote);

        return newNote;
    }

    public Note modifyNote(String title, String body, User user, Integer noteId) {
        Note newNote = getNoteById(user, noteId);

        newNote.setTitle(title);
        newNote.setBody(body);
        newNote.setModifiedDate(Date.from(Instant.now()));

        noteRepo.save(newNote);

        return newNote;
    }

//    this.noteRepo.findAllByUserId((User) session.getAttribute("currentUser"))

    public Iterable<Note> getAllNotes(User user){
        return noteRepo.findAllByUserId(user.getId());
    }

    public Note getNoteById(User user, Integer id){
        if(user == null){
            throw new UserIsNullException("User is null, you need to login first");
        }

        Note newNote = noteRepo.findById(id).orElse(null);

        if(newNote == null){
            System.out.println("Note doesnt exist");
            throw new NoteNotFoundException("Note " + id + " doesnt exist");
        }

        if(newNote.getUser().getId() != user.getId()){
            System.out.println("Note owner mismatch");
            throw new NoteOwnerMismatchException("This note [" + newNote.noteId + "] is not owned by current user [" + user.getId() + "]");
        }

        return newNote;
    }

    @Transactional
    public int deleteById(User user, Integer id){
        Note noteToDelete = getNoteById(user, id);
        noteRepo.delete(noteToDelete);
        return id;
    }
}
