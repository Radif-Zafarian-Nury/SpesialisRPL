package com.example.spesialisRPL.Dokter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DokterCard {
    private int id;
    private String nama;
    private String foto;
    private String spesialisasi;    
}
