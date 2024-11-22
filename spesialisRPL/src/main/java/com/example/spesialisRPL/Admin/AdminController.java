package com.example.spesialisRPL.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/")
    public String index(){
        return "Admin/admin";
    }

    @GetMapping("/daftarpasien")
    public String daftarPasien(){
        return "Admin/admin_daftarPasien";
    }
}
