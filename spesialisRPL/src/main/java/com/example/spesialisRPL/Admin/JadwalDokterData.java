package com.example.spesialisRPL.Admin;

import lombok.Data;

@Data
public class JadwalDokterData {
    
    private String nama;
    private String nama_spesialisasi;
    private String tanggal;
    private String waktu_mulai;
    private String waktu_selesai;

    public JadwalDokterData(String nama, String nama_spesialisasi, String tanggal, String waktu_mulai, String waktu_selesai) {
        this.nama = nama;
        this.nama_spesialisasi = nama_spesialisasi;
        this.tanggal = tanggal;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
    }
}
