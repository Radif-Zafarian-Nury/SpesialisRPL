package com.example.spesialisRPL.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

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

    @GetMapping("/spesialisMata")
    public String spesialisMata() {
        return "User/spesialis_mata";
    } 

    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute UserData userData, 
        Model model,
        BindingResult bindingResult){
        
        //Check validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "User/register";
        }

        //Check NIK
        // if (userRepository.findByNik(userData.getNik()).isPresent()) {
        //     model.addAttribute("error", "NIK sudah terdaftar");
        //     return "User/register";
        // }

        if(userData.getNik().length() != 16){
            model.addAttribute("error", "NIK harus 16 digit");
            return "User/register";
        }

        //Check email
        if (!userData.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            model.addAttribute("error", "Email tidak valid");
            return "User/register";
        }

        //Check password
        if(!userData.getKata_sandi().equals(userData.getConfpassword())){
            model.addAttribute("error", "Passwords do not match!");
            return "User/register";
        }

        userData.setPeran("pasien");
        userRepository.saveUser(userData);
        return "redirect:/user/login";
    }

    
}
