package com.example.spesialisRPL.RekamMedis;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.spesialisRPL.RequiredRole;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/diagnosa")
public class RekamMedisController {
    @Autowired
    private final RekamMedisRepository repo;

    public RekamMedisController(RekamMedisRepository repo){
        this.repo = repo;
    }

    @GetMapping("/{idPasien}")
    @RequiredRole({"dokter", "perawat"})
    public String getRekamMedis(@PathVariable int idPasien, Model model, HttpSession session) {
        List<RekamMedis> rekamMedisList = repo.findByIdPasien(idPasien);

        //Ambil role
        String role = (String) session.getAttribute("role");

        // Periksa jika rekamMedisList kosong
        if (rekamMedisList.isEmpty()) {
            model.addAttribute("rekamMedisList", null);
        } else {
            model.addAttribute("rekamMedisList", rekamMedisList);
        }
        RekamMedis terbaru = repo.getRekamMedisTerbaru(idPasien);
        if(terbaru==null){
            terbaru = new RekamMedis();
        }
        RekamMedis pasienInfo = rekamMedisList.isEmpty() ? null : rekamMedisList.get(0);

        model.addAttribute("role", role);
        model.addAttribute("rekamMedisList", rekamMedisList);
        model.addAttribute("idPasien", idPasien);
        model.addAttribute("pasienInfo", pasienInfo); 
        model.addAttribute("terbaru", terbaru);
        return "TenagaMedis/diagnosa"; 
    }


    @PostMapping("/rekam-medis")
    public String addRekamMedis(@ModelAttribute RekamMedis rekamMedis, @RequestParam(required = false) MultipartFile foto_informasi_tambahan, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (foto_informasi_tambahan != null && !foto_informasi_tambahan.isEmpty()) {
            try {
                byte[] fotoBytes = foto_informasi_tambahan.getBytes();
                String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                rekamMedis.setInformasi_tambahan(fotoBase64);  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        //if perawat
        if(role.equals("perawat")){
            repo.save(rekamMedis);
        }
        else if (role.equals("dokter")){
            repo.updateDariDokter(rekamMedis);
        }

        return "redirect:/diagnosa/" + rekamMedis.getId_pasien();
    }

        @GetMapping("/detail/{id}")
        public String getRekamMedisDetail(@PathVariable int id, Model model) {
            RekamMedis rekamMedis = repo.getRekamMedisById(id);
            
            if (rekamMedis != null) {
                model.addAttribute("informasi_tambahan", rekamMedis.getInformasi_tambahan());
            } else {
                model.addAttribute("informasi_tambahan", null);
            }
            
            return "TenagaMedis/detail";
        }
}

