package ru.kpfu.itis.kropinov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.kropinov.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {

        try {
            userService.register(email, password);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verification")
    public String verify(@RequestParam("code") String verificationCode) {
        System.out.println(">>> verification endpoint hit, code = " + verificationCode);
        if (userService.verifyUser(verificationCode)) {
            return "email-verified";
        }
        return "verification-error";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // for simply testing
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

}
