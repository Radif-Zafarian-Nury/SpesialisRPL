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

    @GetMapping("/form_pilih_dokter_mata")
    public String showDokterMataForm(@RequestParam("id_dokter") int id_dokter, @RequestParam("tanggal") Date tanggal, Model model) {
        Optional<DokterCardSelection> foundDokter = this.userRepository.findDokterMataById(id_dokter, tanggal);
        model.addAttribute("doctor", foundDokter.get());
        return "User/form_pesan_dokter_mata";
    }

    @GetMapping("/form_pilih_dokter_gigi")
    public String showDokterGigiForm(@RequestParam("id_dokter") int id_dokter, @RequestParam("tanggal") Date tanggal, Model model) {
        Optional<DokterCardSelection> foundDokter = this.userRepository.findDokterGigiById(id_dokter, tanggal);
        model.addAttribute("doctor", foundDokter.get());
        return "User/form_pesan_dokter_gigi";
    }

    @GetMapping("/form_pilih_dokter_tht")
    public String showDokterThtForm(@RequestParam("id_dokter") int id_dokter, @RequestParam("tanggal") Date tanggal, Model model) {
        Optional<DokterCardSelection> foundDokter = this.userRepository.findDokterThtById(id_dokter, tanggal);
        model.addAttribute("doctor", foundDokter.get());
        return "User/form_pesan_dokter_tht";
    }

    @GetMapping("/form_pilih_dokter_jantung")
    public String showDokterJantungForm(@RequestParam("id_dokter") int id_dokter, @RequestParam("tanggal") Date tanggal, Model model) {
        Optional<DokterCardSelection> foundDokter = this.userRepository.findDokterJantungById(id_dokter, tanggal);
        model.addAttribute("doctor", foundDokter.get());
        return "User/form_pesan_dokter_jantung";
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

    @GetMapping("/spesialisGigi")
    public String spesialisGigi(@RequestParam(value = "date", required = false) LocalDate date, Model model) {
        List<DokterCardSelection> listDokterGigi = this.userRepository.findFilteredDokterGigi(date);
        model.addAttribute("dokter_gigi", listDokterGigi);
        return "User/spesialis_gigi";
    }

    @GetMapping("/spesialisTht")
    public String spesialisTht(@RequestParam(value = "date", required = false) LocalDate date, Model model) {
        List<DokterCardSelection> listDokterTht = this.userRepository.findFilteredDokterTht(date);
        model.addAttribute("dokter_tht", listDokterTht);
        return "User/spesialis_tht";
    }

    @GetMapping("/spesialisJantung")
    public String spesialisJantung(@RequestParam(value = "date", required = false) LocalDate date, Model model) {
        List<DokterCardSelection> listDokterTht = this.userRepository.findFilteredDokterJantung(date);
        model.addAttribute("dokter_jantung", listDokterTht);
        return "User/spesialis_jantung";
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

    @PostMapping("/pesan_dokter_mata")
    public String pesanDokterMata(@RequestParam("nik") String nik, @RequestParam("id_jadwal") int id_jadwal, HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        int idPasien = this.userRepository.getPatientIdByNik(nik);
        this.userRepository.daftarPasien(idPasien, id_jadwal);
        return "redirect:/user/spesialisMata";
    }

    @PostMapping("/pesan_dokter_gigi")
    public String pesanDokterGigi(@RequestParam("nik") String nik, @RequestParam("id_jadwal") int id_jadwal, HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        int idPasien = this.userRepository.getPatientIdByNik(nik);
        this.userRepository.daftarPasien(idPasien, id_jadwal);
        return "redirect:/user/spesialisGigi";
    }

    @PostMapping("/pesan_dokter_tht")
    public String pesanDokterTht(@RequestParam("nik") String nik, @RequestParam("id_jadwal") int id_jadwal, HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        int idPasien = this.userRepository.getPatientIdByNik(nik);
        this.userRepository.daftarPasien(idPasien, id_jadwal);
        return "redirect:/user/spesialisTht";
    }

    @PostMapping("/pesan_dokter_jantung")
    public String pesanDokterJantung(@RequestParam("nik") String nik, @RequestParam("id_jadwal") int id_jadwal, HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        int idPasien = this.userRepository.getPatientIdByNik(nik);
        this.userRepository.daftarPasien(idPasien, id_jadwal);
        return "redirect:/user/spesialisJantung";
    }
}
