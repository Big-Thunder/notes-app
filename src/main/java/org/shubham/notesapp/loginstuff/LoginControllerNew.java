package org.shubham.notesapp.loginstuff;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class LoginControllerNew {

    private final LoginServiceNew LoginServiceNew;
    private final HttpSession session;

    // Data transfer objects - help us in hiding actual user object from being exposed to frontend
    public record RegisterRequest(String name, String email, String pass) {}
    public record LoginRequest(String email, String pass) {}
    public record UserResponse(int id, String name, String email) {
        public static UserResponse from(User u){
            return new UserResponse(u.getId(), u.getName(), u.getEmail());
        }
    }

    @Autowired
    public LoginControllerNew(LoginServiceNew LoginServiceNew, HttpSession session){
        this.LoginServiceNew = LoginServiceNew;
        this.session = session;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        User newUser = LoginServiceNew.register(request.name(), request.email(), request.pass());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request){
        User user = LoginServiceNew.login(request.email(), request.pass());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUser(){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }
}
