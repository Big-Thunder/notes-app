package org.shubham.notesapp;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.shubham.notesapp.loginstuff.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerNewTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateNote() throws Exception {
        // Create a mock logged-in user
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        session.setAttribute("currentUser", user);

        String json = "{\"title\":\"My Test Note\",\"body\":\"This is the note body\"}";

        mockMvc.perform(post("/new/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(session))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("My Test Note"))
                .andExpect(jsonPath("$.body").value("This is the note body"));
    }

    @Test
    public void testGetAllNotes() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        session.setAttribute("currentUser", user);

        mockMvc.perform(get("/new")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
