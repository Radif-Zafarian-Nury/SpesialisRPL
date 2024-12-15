package com.example.spesialisRPL.Admin;

import lombok.Data;

@Data
public class JadwalDokterData {
    private int idJadwal;
    private String nama;
    private String nama_spesialisasi;
    private String tanggal;
    private String waktu_mulai;
    private String waktu_selesai;
    private int kuotaMax;
    private int kuotaTerisi;

    public JadwalDokterData() {
    }
    
    public JadwalDokterData(String nama, String nama_spesialisasi, String tanggal, String waktu_mulai, String waktu_selesai) {
        this.nama = nama;
        this.nama_spesialisasi = nama_spesialisasi;
        this.tanggal = tanggal;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
    }

    public JadwalDokterData(int idJadwal, String nama, String nama_spesialisasi, String tanggal, String waktu_mulai, String waktu_selesai) {
        this.idJadwal = idJadwal;
        this.nama = nama;
        this.nama_spesialisasi = nama_spesialisasi;
        this.tanggal = tanggal;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
    }

    public JadwalDokterData(int idJadwal, int kuotaMax, int kuotaTerisi) {
        this.idJadwal = idJadwal;
        this.kuotaMax = kuotaMax;
        this.kuotaTerisi = kuotaTerisi;
    }

    public JadwalDokterData(String tanggal, String waktu_mulai, String waktu_selesai, int kuotaMax) {
        this.tanggal = tanggal;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.kuotaMax = kuotaMax;
    }
    
    public JadwalDokterData(int idJadwal, String tanggal, String waktu_mulai, String waktu_selesai, int kuotaMax, int kuotaTerisi) {
        this.idJadwal = idJadwal;
        this.tanggal = tanggal;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.kuotaMax = kuotaMax;
        this.kuotaTerisi = kuotaTerisi;
    }

}
