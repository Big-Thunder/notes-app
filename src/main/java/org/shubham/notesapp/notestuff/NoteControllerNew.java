package org.shubham.notesapp.notestuff;

import jakarta.servlet.http.HttpSession;
import org.shubham.notesapp.loginstuff.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/notes")
public class NoteControllerNew {

    NoteService noteService;
    HttpSession session;

    // Data transfer objects - we'll wrap Note in a DTO, this help us in hiding actual note object from being exposed to frontend
    public record NoteRequest(String title, String body){};
    public record NoteResponse(int id, String title, String body, Date createdDate, Date modifiedDate){
        public static NoteResponse from(Note n){
            return new NoteResponse(n.getNoteId(), n.getTitle(), n.getBody(), n.getCreatedDate(), n.getModifiedDate());
        }
    }

    @Autowired
    NoteControllerNew(NoteService noteService, HttpSession session){
        this.noteService = noteService;
        this.session = session;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(HttpSession session, @RequestBody NoteRequest request){
        Note newNote = noteService.createNote(request.title(), request.body(), (User) session.getAttribute("currentUser"));
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.from(newNote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(HttpSession session, @RequestBody NoteRequest request, @PathVariable Integer id){
        Note newNote = noteService.modifyNote(request.title(), request.body(), (User) session.getAttribute("currentUser"), id);
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(newNote));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> allNotes(HttpSession session){
        Iterable<Note> noteIterable = noteService.getAllNotes((User) session.getAttribute("currentUser"));
        List<NoteResponse> noteResponseList = StreamSupport.stream(noteIterable.spliterator(), false).map(NoteResponse::from).toList();
        return ResponseEntity.ok(noteResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getOneNote(HttpSession session, @PathVariable Integer id){
        Note newNote = noteService.getNoteById((User) session.getAttribute("currentUser"), id);
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(newNote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(HttpSession session, @PathVariable Integer id){
        noteService.deleteById((User) session.getAttribute("currentUser"), id);
        return ResponseEntity.noContent().build();
    }
}
