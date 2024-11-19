package com.example.spesialisRPL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }
}
