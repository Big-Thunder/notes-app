package org.shubham.notesapp.loginstuff;

import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
