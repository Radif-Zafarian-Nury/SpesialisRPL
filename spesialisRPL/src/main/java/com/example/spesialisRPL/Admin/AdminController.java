package com.example.spesialisRPL.Admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.spesialisRPL.RequiredRole;
import com.example.spesialisRPL.User.UserData;
import com.example.spesialisRPL.User.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService; // Inject UserService


    //HALAMAN UTAMA
    @GetMapping("/")
    //@RequiredRole({"admin"})
    public String index(@RequestParam(value = "tgl", required = false) LocalDate tgl, Model model, HttpSession session){
        
        if(tgl==null){
            tgl = LocalDate.now();
        }
        List<JadwalDokterData> jadwalDokter = adminRepository.findSchedulesByDate(tgl);

        model.addAttribute("tgl", tgl);
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

    //LIST PASIEN
    @GetMapping("/list-pasien")
    public String adminListPasien(@RequestParam(value = "tgl", required = false) LocalDate tgl, @RequestParam(value = "namaPasien", required = false) String namaPasien, Model model){
        if(tgl==null){
            tgl = LocalDate.now();

        }
        List<PasienData> listPasien;

        if (namaPasien == null) {
            listPasien = adminRepository.findPendaftaranByDate(tgl);
        } else {
            listPasien = adminRepository.findPendaftaranByDateAndName(tgl, namaPasien);
            model.addAttribute("name", namaPasien); // Add name to model if it's provided
        }
        
        // Add common attributes to the model
        model.addAttribute("tgl", tgl);
        model.addAttribute("results", listPasien);
    
        return "Admin/admin_listPasien"; // Return the view name
    }

    //AMBIL PENDAFTARAN PASIEN BERDASARKAN NAMA PASIEN DAN DATE www
    @GetMapping("/get-nama_pasien")
    @ResponseBody
    public ResponseEntity<List<PasienData>> getNamaPasien(@RequestParam("name") String name){
        LocalDate tgl = LocalDate.of(2024, 12, 20);
        List<PasienData> listPasien = adminRepository.findPendaftaranByDateAndName(tgl, name);
        return ResponseEntity.ok(listPasien);
    }

    //AMBIL NAMA DOKTER BERDASARKAN NAMA PASIEN
    @GetMapping("/get-nama_dokter")
    @ResponseBody
    public ResponseEntity<List<String>> getNamaDokter(@RequestParam("nama") String nama){
        List<String> jadwal = adminRepository.findDoctorNameByPatientName(nama);
        return ResponseEntity.ok(jadwal);
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
        return "Admin/admin_editdokter";
    }

    @GetMapping("/buatakun")
    public String buatAkun(){
        return "Admin/admin_buatAkunBaru";
    }

    @GetMapping("/halamanEdit/{id}")
    public String showEditDoctorForm(@PathVariable("id") int id, Model model) {
        Dokter dokter = adminRepository.getDokter(id); 
        List<String> spesialisasiDokter = adminRepository.findSpecializationsByDoctorID(id);
        List<String> spesialisasiList = adminRepository.getAllSpesialisasi();

        model.addAttribute("dokter", dokter);
        model.addAttribute("spesialisasi_dokter", spesialisasiDokter);
        model.addAttribute("spesialisasi_list", spesialisasiList);
        return "Admin/admin_halamanEdit";
    }

    @PostMapping("/editdokter")
    public String saveDataEditDokter(@ModelAttribute Dokter dokter, 
                                    @RequestParam List<String> specializations,
                                    @RequestParam(required = false) MultipartFile doctorPhoto) {  

        if (doctorPhoto != null && !doctorPhoto.isEmpty()) {
            try {
                // Convert the image to Base64
                byte[] fotoBytes = doctorPhoto.getBytes();
                String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                dokter.setFoto(fotoBase64);  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        adminRepository.updateDokter(dokter, specializations);

        return "redirect:/admin/editdokter";
    }



    @PostMapping("/buatakun")
    public String buatAkunUser(
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
        
        //cek NIK (length & Harus Angka)
        if(userData.getNik().length() != 16 || userData.getNik().matches("[a-zA-Z]+")){
            model.addAttribute("error", "NIK harus 16 digit nomor");
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
        // userData.setPeran("pasien");
        // userRepository.saveUser(userData);
        return "redirect:/login";
    }
}
