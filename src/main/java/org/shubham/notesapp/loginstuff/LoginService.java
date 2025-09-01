package org.shubham.notesapp.loginstuff;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@RestController
public class LoginService {
    HttpSession session;
    UserRepo userRepo;

    LoginService(HttpSession session, UserRepo userRepo){
        this.session = session;
        this.userRepo = userRepo;
    }

    public User register(RedirectAttributes redirectAttributes, String name, String email, String pass){
        if(userRepo.existsByEmail(email)){
            redirectAttributes.addFlashAttribute("message", "User with this email already exists");
            return null;
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPass(pass);

        userRepo.save(newUser);
        redirectAttributes.addFlashAttribute("message", "User registered");

        return newUser;
    }

    public User login(RedirectAttributes redirectAttributes, String email, String pass){
        User user = userRepo.findByEmail(email);

        if(user == null){
            redirectAttributes.addFlashAttribute("message", "invalid credentials");
            return null;
        }

        if(!Objects.equals(user.getPass(), pass)){
            redirectAttributes.addFlashAttribute("message", "invalid credentials");
            return null;
        }

        session.setAttribute("currentUser", user);
        redirectAttributes.addFlashAttribute("message","logging in");

        System.out.println("Email: " + ((User) session.getAttribute("currentUser")).getEmail() + ", id: " + ((User) session.getAttribute("currentUser")).getId());
        return user;
    }
}
