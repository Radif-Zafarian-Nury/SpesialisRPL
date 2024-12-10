package com.example.spesialisRPL.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Dokter {
    private int id_user; 
    private String nama; 
    private String nik;
    private String foto;    //Base64
    private String alamat;
    private char jenis_kelamin;
}
