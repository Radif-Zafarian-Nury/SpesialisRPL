package com.example.spesialisRPL.Admin;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.spesialisRPL.User.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserService

    @Autowired
    private PasswordEncoder passwordEncoder;

    //HALAMAN UTAMA
    @GetMapping("/")
    @RequiredRole({"admin"})
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
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<String>> getDoctorsByDay(@RequestParam("tanggal") String tanggal){
        List<String> daftarDokter = adminRepository.findDoctorsByDay(tanggal);
        return ResponseEntity.ok(daftarDokter);
    }

    //AMBIL NILAI DOKTER SPESIALISASI BERDASARKAN DOKTER
    @GetMapping("/get-spesialisasi")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<String>> getSpesialisasi(
        @RequestParam("dokter") String dokter,
        @RequestParam("tanggal") String tanggal
    ){
        List<String> spesialisai = adminRepository.findSpecializationsByDoctor(dokter, tanggal);
        return ResponseEntity.ok(spesialisai);
    }

    //AMBIL NILAI JADWAL BERDASARKAN SPESIALIASI
    @GetMapping("/get-jadwal")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<JadwalDokterData>> getJadwal(
        @RequestParam("spesialisasi") String spesialisasi,
        @RequestParam("dokter") String dokter,
        @RequestParam("tanggal") String tanggal
    ){
        List<JadwalDokterData> jadwal = adminRepository.findSchedulesBySpecialization(spesialisasi, dokter, tanggal);
        return ResponseEntity.ok(jadwal);
    }

    //CEK KUOTA DOKTER
    @GetMapping("/check-quota")
    @RequiredRole({"admin"})
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
    @RequiredRole({"admin"})
    public String adminListPasien(@RequestParam(value = "tgl", required = false) LocalDate tgl, @RequestParam(value = "namaPasien", required = false) String namaPasien, Model model){
        if(tgl==null){
            tgl = LocalDate.now();
        }
        List<PasienData> listPasien;

        if (namaPasien == null) {
            listPasien = adminRepository.findPendaftaranByDate(tgl);
        } else {
            listPasien = adminRepository.findPendaftaranByDateAndName(tgl, namaPasien);
            model.addAttribute("name", namaPasien); 
        }
        
        model.addAttribute("tgl", tgl);
        model.addAttribute("results", listPasien);
    
        return "Admin/admin_listPasien"; // Return the view name
    }

    @GetMapping("/check-id_pendaftaran")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<PasienData>> checkIdPendaftaran(@RequestParam("id") int id){
        
        List<PasienData> pendaftaran = adminRepository.updatePembayaran(id);
        return ResponseEntity.ok(pendaftaran);
    }

    @GetMapping("/daftar-ulang")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<PasienData>> daftarUlang(@RequestParam("id") int id){
        
        List<PasienData> pendaftaran = adminRepository.updateDaftarUlang(id);
        return ResponseEntity.ok(pendaftaran);
    }

    //AMBIL PENDAFTARAN PASIEN BERDASARKAN NAMA PASIEN DAN DATE www
    @GetMapping("/get-nama_pasien")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<PasienData>> getNamaPasien(@RequestParam("name") String name){
        LocalDate tgl = LocalDate.of(2024, 12, 20);
        List<PasienData> listPasien = adminRepository.findPendaftaranByDateAndName(tgl, name);
        return ResponseEntity.ok(listPasien);
    }

    //AMBIL NAMA DOKTER BERDASARKAN NAMA PASIEN
    @GetMapping("/get-nama_dokter")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<List<String>> getNamaDokter(@RequestParam("nama") String nama){
        List<String> jadwal = adminRepository.findDoctorNameByPatientName(nama);
        return ResponseEntity.ok(jadwal);
    }

    @GetMapping("/daftarpasien")
    @RequiredRole({"admin"})
    public String daftarPasien(){
        return "Admin/admin_daftarPasien";
    }

    @PostMapping("/register-pasien")
    @RequiredRole({"admin"})
    @ResponseBody
    public ResponseEntity<String> registerPasien(
        @RequestParam("nik") String nik, 
        @RequestParam("idJadwal") int idJadwal,
        @RequestParam("spesialisasi") String spesialisasi
    ){
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
            adminRepository.registerPasien(nik, idJadwal, spesialisasi);
            adminRepository.incrementKuotaTerisi(idJadwal);
            return ResponseEntity.ok("Pasien berhasil didaftarkan");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal mendaftarkan pasien");
        }
    }

    @GetMapping("/check-nik")
    @RequiredRole({"admin"})
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
    @RequiredRole({"admin"})
    public String editDokter(Model model){
        List<DokterCard> listCards = adminRepository.getAllDoctorCards();
        model.addAttribute("dokter_list", listCards);
        return "Admin/admin_editdokter";
    }

    @GetMapping("/buatakun")
    @RequiredRole({"admin"})
    public String buatAkun(){
        return "Admin/admin_buatAkunBaru";
    }

    @GetMapping("/halamanEdit/{id}")
    @RequiredRole({"admin"})
    public String showEditDoctorForm(@PathVariable("id") int id, Model model) {
        Dokter dokter = adminRepository.getDokter(id); 
        List<String> spesialisasiDokter = adminRepository.findSpecializationsByDoctorID(id);
        List<String> spesialisasiList = adminRepository.getAllSpesialisasi();

        LocalDate tgl = LocalDate.now();
        ArrayList<JadwalDokterData> jadwalList = adminRepository.getFutureJadwalByDoctorID(id, tgl); 
        if (jadwalList == null) {
            jadwalList = new ArrayList<>();
        }

        JadwalDokterDataWrapper wrapper = new JadwalDokterDataWrapper(jadwalList);

        model.addAttribute("dokter", dokter);
        model.addAttribute("spesialisasi_dokter", spesialisasiDokter);
        model.addAttribute("spesialisasi_list", spesialisasiList);
        model.addAttribute("wrapper", wrapper);
        return "Admin/admin_halamanEdit";
    }

    @PostMapping("/editdokter")
    @RequiredRole({"admin"})
    public String saveDataEditDokter(@ModelAttribute Dokter dokter, 
                                    @RequestParam List<String> specializations,
                                    @RequestParam(required = false) MultipartFile doctorPhoto,
                                    @ModelAttribute JadwalDokterDataWrapper wrapper,
                                    @RequestParam(required = false) List<Integer> idsToDelete) {
                                
        if (doctorPhoto != null && !doctorPhoto.isEmpty()) {
            try {
                byte[] fotoBytes = doctorPhoto.getBytes();
                String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                dokter.setFoto(fotoBase64);  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<JadwalDokterData> listJadwal = wrapper.getListJadwal();
        
        adminRepository.updateDokter(dokter, specializations, listJadwal);

        if (idsToDelete != null && !idsToDelete.isEmpty()) {
            for (Integer id : idsToDelete) {
                adminRepository.deleteJadwalById(id); 
            }
        }
        return "redirect:/admin/editdokter";
    }

    @PostMapping("/buatakun")
    @RequiredRole({"admin"})
    public String buatAkunUser(
        @Valid @ModelAttribute UserData userData, 
        Model model,
        BindingResult bindingResult) throws ParseException{
            
        //Check validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Silakan perbaiki kesalahan berikut:");
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "Admin/admin_buatAkunBaru"; // Ganti dengan nama file HTML yang sesuai
        }

        
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

        

        userData.setKata_sandi(passwordEncoder.encode(userData.getKata_sandi()));
        userRepository.saveUserDariAdmin(userData);

        return "redirect:/admin/";
    }
}
