package com.example.spesialisRPL.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.spesialisRPL.Dokter.DokterCard;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    //HALAMAN UTAMA
    @GetMapping("/")
    // @RequiredRole({"admin"})
    public String index(Model model){
        //List<JadwalDokterData> jadwalDokter = adminRepository.findAll();
        //model.addAttribute("results", jadwalDokter);
        return "Admin/admin";
    }

    //AMBIL NILAI DOKTER BERDASARKAN HARI
    @GetMapping("/get-dokter")
    @ResponseBody
    public ResponseEntity<List<String>> getDoctorsByDay(@RequestParam("tanggal") String tanggal){
        List<String> daftarDokter = adminRepository.findDoctorsByDay(tanggal);
        return ResponseEntity.ok(daftarDokter);
    }

    //AMBIL NILAI DOKTER SPESIALISASI BERDASARKAN DOKTER
    @GetMapping("/get-spesialisasi")
    @ResponseBody
    public ResponseEntity<List<String>> getSpesialisasi(@RequestParam("dokter") String dokter){
        List<String> spesialisai = adminRepository.findSpecializationsByDoctor(dokter);
        return ResponseEntity.ok(spesialisai);
    }

    //AMBIL NILAI JADWAL BERDASARKAN SPESIALIASI
    @GetMapping("/get-jadwal")
    @ResponseBody
    public ResponseEntity<List<JadwalDokterData>> getJadwal(@RequestParam("spesialisasi") String spesialisasi){
        List<JadwalDokterData> jadwal = adminRepository.findSchedulesBySpecialization(spesialisasi);
        return ResponseEntity.ok(jadwal);
    }

    //CEK KUOTA DOKTER
    @GetMapping("/check-quota")
    @ResponseBody
    public ResponseEntity<?> checkQuota(@RequestParam("idJadwal") int idJadwal){
        JadwalDokterData jadwal = adminRepository.findScheduleById(idJadwal);
        if(jadwal == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jadwal tidak ditemukan");
        }

        return ResponseEntity.ok(new QuotaResponse(jadwal.getKuotaTerisi(), jadwal.getKuotaMax()));
    }

    @GetMapping("/daftarpasien")
    public String daftarPasien(){
        return "Admin/admin_daftarPasien";
    }

    @PostMapping("/register-pasien")
    @ResponseBody
    public ResponseEntity<String> registerPasien(@RequestParam("nik") String nik, @RequestParam("idJadwal") int idJadwal){
        //Validasi nik
        if (nik == null || nik.length() != 16 || !nik.matches("\\d+")) {
            return ResponseEntity.badRequest().body("NIK tidak valid.");
        }
        
        //Cek nik di db
        var pasien = adminRepository.findNik(nik);
        if(pasien.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pasien belum memilki akun");
        }

        //Mendaftarkan pasien
        try {
            adminRepository.registerPasien(nik, idJadwal);
            adminRepository.incrementKuotaTerisi(idJadwal);
            return ResponseEntity.ok("Pasien berhasil didaftarkan");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal mendaftarkan pasien");
        }
    }

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
    public String editDokter(Model model){
        List<DokterCard> listCards = adminRepository.getAllDoctorCards();
        model.addAttribute("dokter_list", listCards);
        return "Admin/admin_editDokter";
    }

    @GetMapping("/buatakun")
    public String buatAkun(){
        return "Admin/admin_buatAkunBaru";
    }

    @GetMapping("/halamanedit")
    public String halamanEdit(){
        return "Admin/admin_halamanEdit";
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
