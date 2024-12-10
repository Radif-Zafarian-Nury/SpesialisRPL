package com.example.spesialisRPL.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/")
    // @RequiredRole({"admin"})
    public String index(Model model){
        //List<JadwalDokterData> jadwalDokter = adminRepository.findAll();
        //model.addAttribute("results", jadwalDokter);
        return "Admin/admin";
    }

    @GetMapping("/daftarpasien")
    public String daftarPasien(){
        return "Admin/admin_daftarPasien";
    }

    // @PostMapping("/daftarpasien")
    // public String formPasien(@Valid @ModelAttribute FormPendaftaranData formData){
    //     //Check NIK
    //     if(adminRepository.findNik(formData.getNik()).isPresent()){
    //         //Tampilkan tanggal Lahir, jenis kelamin dan nomor telp (jika ada)
    //     } else {
    //         //Tidak terdaftar keluarkan error "Pasien belum terdaftar"
    //     }

    //     //Tambahin pop up kalau udah berhasil daftar
    //     return "redirect:/Admin/daftarpasien";
    // }

    @GetMapping("/check-nik")
    @ResponseBody
    public ResponseEntity<?> checkNik(@RequestParam("nik") String nik){
        //Coba ganti jangan var
        var pasien = adminRepository.findNik(nik);
        if(pasien.isPresent()){
            return ResponseEntity.ok(pasien.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pasien belum terdaftar");
        }
    }

    @GetMapping("/editdokter")
    public String editDokter(){
        return "Admin/admin_editDokter";
    }

    @GetMapping("/buatakun")
    public String buatAkun(){
        return "Admin/admin_buatAkunBaru";
    }

    // @PostMapping("/buatakun")
    // public String buatAkunUser(
    //     @Valid @ModelAttribute UserData userData, 
    //     Model model,
    //     BindingResult bindingResult){
            
        
    //     //Check validation
    //     if (bindingResult.hasErrors()) {
    //         model.addAttribute("error", "Please correct the highlighted errors.");
    //         return "User/register";
    //     }

    //     //Check NIK
    //     // if (userRepository.findByNik(userData.getNik()).isPresent()) {
    //     //     model.addAttribute("error", "NIK sudah terdaftar");
    //     //     return "User/register";
    //     // }
        
    //     //cek NIK (length & Harus Angka)
    //     if(userData.getNik().length() != 16 || userData.getNik().matches("[a-zA-Z]+")){
    //         model.addAttribute("error", "NIK harus 16 digit nomor");
    //         return "User/register";
    //     }

    //     //Check email
    //     if (!userData.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
    //         model.addAttribute("error", "Email tidak valid");
    //         return "User/register";
    //     }

    //     //Check password
    //     if(!userData.getKata_sandi().equals(userData.getConfpassword())){
    //         model.addAttribute("error", "Passwords do not match!");
    //         return "User/register";
    //     }

    //     boolean isRegistered = userService.register(userData);
    //     if (!isRegistered) {
    //         model.addAttribute("error", "Registration failed. Please try again.");
    //         return "User/register";
    //     }
    //     // userData.setPeran("pasien");
    //     // userRepository.saveUser(userData);
    //     return "redirect:/login";
    // }
}
