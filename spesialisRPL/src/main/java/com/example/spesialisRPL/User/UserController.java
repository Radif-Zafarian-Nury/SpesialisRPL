package com.example.spesialisRPL.User;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spesialisRPL.Doctor.DokterCardSelection;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService; // Inject UserService
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
   
    @GetMapping("/")
    public String index(HttpSession session){
        if(session.getAttribute("user") == null){   //kalo belom login ke index
            return "User/index";
        }
        
        //UserData loggedInUser  = (UserData) session.getAttribute("loggedInUser ");
        //logger.info("User  role: {}", loggedInUser.getPeran()); // Log the user's role
        return "User/LoggedInIndex"; //kalo udh ke yg logged in
    }

    @GetMapping("/form_pilih_dokter")
    public String showDokterForm() {
        return "User/form_pesan_dokter";
    }
    
    @GetMapping("/register")
    public String register(){
        return "User/register";
    }

    @GetMapping("/spesialisMata")
    public String spesialisMata(@RequestParam(value = "date", required = false) LocalDate date, Model model) {
        List<DokterCardSelection> listDokterMata = this.userRepository.findFilteredDokterMata(date);
        model.addAttribute("dokter_mata", listDokterMata);
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
        
        //cek length sama ada huruf ato ga
        if(userData.getNik().length() != 16 || userData.getNik().matches("[a-zA-Z]+")){
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

        boolean isRegistered = userService.register(userData);
        if (!isRegistered) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "User/register";
        }
        userData.setPeran("pasien");
        // userRepository.saveUser(userData);
        return "redirect:/login";
    }
}
