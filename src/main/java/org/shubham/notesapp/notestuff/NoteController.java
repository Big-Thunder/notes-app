package org.shubham.notesapp.notestuff;

import jakarta.servlet.http.HttpSession;
import org.shubham.notesapp.loginstuff.User;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    private final NoteRepo noteRepo;
    HttpSession session;
    NoteService noteService;

    NoteController(HttpSession session, NoteRepo noteRepo, NoteService noteService){
        this.session = session;
        this.noteRepo = noteRepo;
        this.noteService = noteService;
    }

    @GetMapping("/note")
    public String noteHome( Model model){
        model.addAttribute("allNotes", noteService.getAllNotes((User) session.getAttribute("currentUser")));
        return "noteHome";
    }

    @PostMapping("/note")
    public String createNote(@RequestParam("body") String body, @RequestParam("title") String title, Model model){
        model.addAttribute("updateMethod", "create");
        noteService.createNote(title, body, (User) session.getAttribute("currentUser"));
        return "redirect:/note";
    }

    @GetMapping("/noteModify")
    public String noteModifyPage(@RequestParam(required = false) Integer id, Model model){
        Note note;

        if(id != null){
            note = noteRepo.findById(id).orElse(new Note());
        }else{
            note = new Note();
        }

        model.addAttribute("note", note);
        return "noteModify";
    }

    @PostMapping("/noteModify") //Update
    public String noteModifyUpdate(@RequestParam("title") String title, @RequestParam("body") String body, @RequestParam Integer noteId, Model model) {
        noteService.modifyNote(title, body, (User) session.getAttribute("currentUser"), noteId);
        return "redirect:/note";
    }
}
