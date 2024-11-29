package com.example.spesialisRPL.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/")
    public String index(Model model){
        List<JadwalDokterData> jadwalDokter = adminRepository.findAll();
        model.addAttribute("results", jadwalDokter);
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
}
