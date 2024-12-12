package com.example.spesialisRPL.Pasien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.spesialisRPL.RequiredRole;

@Controller
public class PasienController {
    @Autowired
    private PasienService pasienService;

    @GetMapping("/listPasien")
    @RequiredRole({"dokter", "perawat"})
    public String listPatients(Model model) {
        List<Pasien> pasienList = pasienService.getAllpasiens();

        model.addAttribute("patients", pasienList);
        return "TenagaMedis/home";
    }

}
