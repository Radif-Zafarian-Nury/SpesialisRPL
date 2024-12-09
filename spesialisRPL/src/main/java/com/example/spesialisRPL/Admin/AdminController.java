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

import com.example.spesialisRPL.Dokter.DokterCard;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    //HALAMAN UTAMA
    @GetMapping("/")
    public String index(Model model){
        List<JadwalDokterData> jadwalDokter = adminRepository.findAll();
        model.addAttribute("results", jadwalDokter);
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

    @GetMapping("/daftarpasien")
    public String daftarPasien(){
        return "Admin/admin_daftarPasien";
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
}
