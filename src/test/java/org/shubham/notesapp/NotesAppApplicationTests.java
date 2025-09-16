package org.shubham.notesapp;

import org.junit.jupiter.api.Test;
import org.shubham.notesapp.loginstuff.User;
import org.shubham.notesapp.notestuff.Note;
import org.shubham.notesapp.notestuff.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NotesAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepo noteRepo;

    // --- Authentication tests ---

    @Test
    void userCanRegisterAndLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "alice")
                        .param("email", "test@mail.com")
                        .param("pass", "pass123"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/login")
                        .param("email", "test@mail.com")
                        .param("pass", "pass123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void loginFailsWithIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "bob")
                        .param("email", "bob@mail.com")
                        .param("pass", "secret"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/login")
                        .param("email", "bob@mail.com")
                        .param("pass", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // --- Notes CRUD tests ---
    @Test
    void authenticatedUserCanCreateViewUpdateDeleteNote() throws Exception {
        // Register
        mockMvc.perform(post("/register")
                        .param("name", "carol")
                        .param("email", "carol@mail.com")
                        .param("pass", "pass"))
                .andExpect(status().is3xxRedirection());

        // Login + capture session
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .param("email", "carol@mail.com")
                        .param("pass", "pass"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();
        assertNotNull(session.getAttribute("currentUser"));

        // Create note
        mockMvc.perform(post("/note")
                        .param("title", "Test Note")
                        .param("body", "Initial content")
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Note createdNote = noteRepo.findAll().iterator().next();
        Integer noteId = createdNote.getNoteId(); // ✅ use correct getter

        // View note
        mockMvc.perform(get("/noteModify")
                        .param("id", noteId.toString())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Note")))
                .andExpect(content().string(containsString("Initial content")));

        // Update note
        mockMvc.perform(post("/noteModify")
                        .param("noteId", noteId.toString())
                        .param("title", "Updated Note")
                        .param("body", "Updated content")
                        .session(session))
                .andExpect(status().is3xxRedirection());

        // Confirm update
        mockMvc.perform(get("/noteModify")
                        .param("id", noteId.toString())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated Note")))
                .andExpect(content().string(containsString("Updated content")));
    }
}
