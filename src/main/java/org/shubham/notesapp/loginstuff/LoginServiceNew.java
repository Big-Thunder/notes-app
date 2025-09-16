package org.shubham.notesapp.loginstuff;

import jakarta.servlet.http.HttpSession;
import org.shubham.notesapp.exceptionstuff.exceptions.EmailAlreadyExistsException;
import org.shubham.notesapp.exceptionstuff.exceptions.InvalidCredentialsException;
import org.shubham.notesapp.exceptionstuff.exceptions.UserIsNullException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceNew {
    private HttpSession session;
    private UserRepo userRepo;

    LoginServiceNew(HttpSession session, UserRepo userRepo){
        this.session = session;
        this.userRepo = userRepo;
    }

    public User register(String name, String email, String pass){
        if(userRepo.existsByEmail(email)){
            throw new EmailAlreadyExistsException(email + " already exists");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPass(pass);

        userRepo.save(newUser);

        return newUser;
    }

    public User login(String email, String pass){
        User user = userRepo.findByEmail(email);

        if(user == null || !Objects.equals(user.getPass(), pass)){
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        session.setAttribute("currentUser", user);

        return user;
    }
}
