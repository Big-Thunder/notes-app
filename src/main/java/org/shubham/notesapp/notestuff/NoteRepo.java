package org.shubham.notesapp.notestuff;

import org.shubham.notesapp.loginstuff.User;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepo extends CrudRepository<Note, Integer> {
    Iterable<Note> findAllByUserId(int user_id);
}
