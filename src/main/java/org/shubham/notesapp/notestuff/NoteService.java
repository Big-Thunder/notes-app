package org.shubham.notesapp.notestuff;

import jakarta.servlet.http.HttpSession;
import org.shubham.notesapp.loginstuff.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.sql.Date;

@Service
public class NoteService {
    HttpSession session;
    NoteRepo noteRepo;

    NoteService(HttpSession session, NoteRepo noteRepo){
        this.session = session;
        this.noteRepo = noteRepo;
    }

    public Note createNote(String title, String body){
        System.out.println("Creating new note for" + ((User) session.getAttribute("currentUser")).getName());

        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setBody(body);
        newNote.setUserId((User) session.getAttribute("currentUser"));
        newNote.setCreatedDate(Date.valueOf(LocalDate.now()));
        newNote.setModifiedDate(Date.valueOf(LocalDate.now()));

        noteRepo.save(newNote);

        return newNote;
    }

    public Note modifyNote(String title, String body, User user, Integer noteId){

        if(!noteRepo.existsById(noteId)){
            System.out.println("Note doesnt exist");
            createNote(title, body);
            return null;
        }

        Note newNote = noteRepo.findById(noteId).get();
        if(newNote.getUserId().getId() != user.getId()){
            System.out.println("Note owner mismatch stuff");
            return null;

        }

        newNote.setTitle(title);
        newNote.setBody(body);
        newNote.setModifiedDate(Date.valueOf(LocalDate.now()));

        noteRepo.save(newNote);

        return newNote;
    }
}
