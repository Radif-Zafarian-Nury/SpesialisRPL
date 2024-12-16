package com.example.spesialisRPL;

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


    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView(HttpSession session){
        if(session.getAttribute("user") != null){  //kalo udah login balikin ke user
            return "redirect:/user/";
        }
        return "User/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        

        UserData user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getPeran());
            

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
