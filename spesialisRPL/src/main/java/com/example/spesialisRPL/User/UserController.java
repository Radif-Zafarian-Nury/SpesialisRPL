package com.example.spesialisRPL.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
   
    @GetMapping("/")
    public String index(HttpSession session){
        return "User/index";
    }

    @GetMapping("/form_pilih_dokter")
    public String showDokterForm(@RequestParam("id_dokter") int id_dokter, @RequestParam("tanggal") Date tanggal, Model model) {
        Optional<DokterCardSelection> foundDokter = this.userRepository.findDokterMataById(id_dokter, tanggal);
        model.addAttribute("doctor", foundDokter.get());
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

    // @PostMapping("/pesan_dokter")
    // public String submitOrderDokterForm(@RequestMapping ) {

    // }

    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute UserData userData, 
        Model model,
        BindingResult bindingResult) throws ParseException{
        
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

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // LocalDate tanggal = userData.getTanggal_lahir();

        boolean isRegistered = userService.register(userData);
        if (!isRegistered) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "User/register";
        }
        // userData.setPeran("pasien");
        // userRepository.saveUser(userData);
        return "redirect:/login";
    }

    @PostMapping("/pesan_dokter")
    public String pesanDokter(@RequestParam("nik") String nik, @RequestParam("id_jadwal") int id_jadwal, HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        int idPasien = this.userRepository.getPatientIdByNik(nik);
        this.userRepository.daftarPasien(idPasien, id_jadwal);
        return "redirect:/user/spesialisMata";
    }
}
