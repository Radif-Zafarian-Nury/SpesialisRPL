package com.example.spesialisRPL.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/")
    public String index(){
        return "User/index";
    }

    @GetMapping("/login")
    public String login(){
        return "User/login";
    }

    @GetMapping("/register")
    public String register(){
        return "User/register";
    }

    @PostMapping("/register")
    public String registerUser(
        @ModelAttribute UserData userData, 
        Model model,
        BindingResult bindingResult){
            
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "User/register";
        }

        if(!userData.getKata_sandi().equals(userData.getConfpassword())){
            model.addAttribute("error", "Passwords do not match!");
            return "User/register";
        }

        userRepository.saveUser(userData);
        return "redirect:/user/login";
    } 
}
