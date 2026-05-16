package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.User;
import com.popcorn.popcorn1.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public RegisterController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        model.addAttribute("addAuthCss", true);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password_confirmation,
            Model model) {

        if (password == null || password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            model.addAttribute("addAuthCss", true);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "register";
        }
        if (!password.equals(password_confirmation)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("addAuthCss", true);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "register";
        }
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email is already registered.");
            model.addAttribute("addAuthCss", true);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "register";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("user");
        userRepository.save(user);

        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/";
    }
}
