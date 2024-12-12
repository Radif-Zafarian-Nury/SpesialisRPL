package com.example.spesialisRPL.RekamMedis;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/diagnosa")
public class RekamMedisController {
    @Autowired
    private final RekamMedisRepository repo;

    public RekamMedisController(RekamMedisRepository repo){
        this.repo = repo;
    }

    @GetMapping("/{idPasien}")
    public String getRekamMedis(@PathVariable int idPasien, Model model) {
        List<RekamMedis> rekamMedisList = repo.findByIdPasien(idPasien);

        // Periksa jika rekamMedisList kosong
        if (rekamMedisList.isEmpty()) {
            model.addAttribute("rekamMedisList", null);
        } else {
            model.addAttribute("rekamMedisList", rekamMedisList);
        }

        RekamMedis pasienInfo = rekamMedisList.isEmpty() ? null : rekamMedisList.get(0);

        model.addAttribute("rekamMedisList", rekamMedisList);
        model.addAttribute("idPasien", idPasien);
        model.addAttribute("pasienInfo", pasienInfo); 
        return "Dokter/diagnosa"; 
    }


    @PostMapping("/rekam-medis")
    public String addRekamMedis(@ModelAttribute RekamMedis rekamMedis) {
        repo.save(rekamMedis);
        return "redirect:/diagnosa/" + rekamMedis.getId_pasien();
    }
}

