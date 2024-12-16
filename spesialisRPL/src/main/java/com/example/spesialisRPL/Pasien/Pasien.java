package com.example.spesialisRPL.Pasien;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pasien {
    private int id_pasien;
    private String nama;
    private String jenis_kelamin;
    private String tanggal_lahir;
    private int no_rekam_medis;
    private int no_antrian;
    private String waktu_mulai;
    private String waktu_selesai;
    
    public String getJadwal() {
        return waktu_mulai + " - " + waktu_selesai;
    }
}
