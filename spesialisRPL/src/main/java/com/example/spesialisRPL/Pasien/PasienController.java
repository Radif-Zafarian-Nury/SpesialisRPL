package com.example.spesialisRPL.Pasien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasienController {
    @Autowired
    private PasienService pasienService;

    @GetMapping("/listPasien")
    public String listPatients(Model model) {
        List<Pasien> pasienList = pasienService.getAllpasiens();

        model.addAttribute("patients", pasienList);
        return "Dokter/home";
    }

}
