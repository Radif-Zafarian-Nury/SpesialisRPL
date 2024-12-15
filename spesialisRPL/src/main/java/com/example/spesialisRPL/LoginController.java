package com.example.spesialisRPL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spesialisRPL.User.UserData;
import com.example.spesialisRPL.User.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView(HttpSession session){
        if(session.getAttribute("user") != null){  //kalo udah login ke user yg loggedin
            return "redirect:/user/";
        }
        return "User/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        logger.info("Attempting to log in with email: {}", email);

        UserData user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getPeran());
            logger.info("Login successful for user: {}", email);    //logger

            String role = user.getPeran();
            return switch (role) {
                case "admin" -> "redirect:/admin/";
                case "dokter" -> "redirect:/listPasien";
                case "perawat" -> "redirect:/listPasien";
                case "pasien" -> "redirect:/user/";
                default -> "redirect:/user/";
            };
        } 
        else {
            logger.warn("Login failed for email: {}", email);   //logger
            model.addAttribute("error", "Invalid email or password");
            return "User/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/"; 
    }

}
