package com.example.spesialisRPL.Pasien;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spesialisRPL.RequiredRole;

@Controller
public class PasienController {
    @Autowired
    private PasienRepository pasienRepository;

    //LIST PASIEN
    @GetMapping("/listPasien")
    @RequiredRole({"dokter", "perawat"})
    public String medisListPasien(@RequestParam(value = "tgl", required = false) LocalDate tgl, @RequestParam(value = "namaPasien", required = false) String namaPasien, Model model){
        
        if(tgl==null){
            tgl = LocalDate.now();
        }
        List<Pasien> listPasien;

        if (namaPasien == null) {
            listPasien = pasienRepository.findPendaftaranByDate(tgl);
        } else {
            listPasien = pasienRepository.findPendaftaranByDateAndName(tgl, namaPasien);
            model.addAttribute("name", namaPasien); 
        }
        
        model.addAttribute("tgl", tgl);
        model.addAttribute("patients", listPasien);
    
        return "TenagaMedis/home"; 
    }
}
