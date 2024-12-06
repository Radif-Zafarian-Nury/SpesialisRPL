package com.example.spesialisRPL.Doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Doctor {
    private String nama;
    private byte[] foto_dokter;
    private String nama_spesialisasi;
}
