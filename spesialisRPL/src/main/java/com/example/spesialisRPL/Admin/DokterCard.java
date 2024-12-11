package com.example.spesialisRPL.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DokterCard {
    private int id;
    private String nama;
    private String foto;    //Base64
    private String spesialisasi;    
}
