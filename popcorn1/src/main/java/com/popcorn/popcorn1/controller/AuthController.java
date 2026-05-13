package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.User;
import com.popcorn.popcorn1.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthController {

    private static final String SESSION_USER_KEY = "user";

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(SESSION_USER_KEY) != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        Optional<User> found = userRepository.findByEmail(email);
        if (found.isEmpty()) {
            return "redirect:/login?error";
        }
        User user = found.get();
        if (!Objects.equals(user.getPassword(), password)) {
            return "redirect:/login?error";
        }

        session.setAttribute(SESSION_USER_KEY, user);
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {

        if (session.getAttribute(SESSION_USER_KEY) != null) {
            return "redirect:/home";
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        // check if email already exists
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return "redirect:/register?error=email";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("user");

        userRepository.save(user);
        session.setAttribute(SESSION_USER_KEY, user);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
