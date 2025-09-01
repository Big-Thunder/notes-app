package org.shubham.notesapp.loginstuff;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class LoginController {
    LoginService loginService;
    HttpSession session;

    LoginController(LoginService loginService, HttpSession session){
        this.loginService = loginService;
        this.session = session;
    }

    @GetMapping("/")
    public  String homePage(){
        return "home";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


    @PostMapping("/register")
    public String register(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("pass") String pass, RedirectAttributes redirectAttributes){
        loginService.register(redirectAttributes, name, email, pass);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("pass") String pass, RedirectAttributes redirectAttributes){
        loginService.login(redirectAttributes, email, pass);
        return "redirect:/note";
    }
}
