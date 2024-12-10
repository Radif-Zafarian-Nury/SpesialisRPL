package com.example.spesialisRPL.Pasien;

import lombok.Data;

@Data
public class Pasien {
    private int id_pasien;
    private String nama;
    private String jenis_kelamin;
    private String tanggal_lahir;
    private String no_rekam_medis;
    private String no_antrian;
    private String waktu_mulai;
    private String waktu_selesai;
    
    public String getJadwal() {
        return waktu_mulai + " - " + waktu_selesai;
    }
}
