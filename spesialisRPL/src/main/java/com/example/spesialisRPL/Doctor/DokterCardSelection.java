    package com.example.spesialisRPL.Doctor;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DokterCardSelection {
    private int id_dokter;
    private String nama;
    private String nama_spesialisasi;
    private String foto_dokter;
    private Date tanggal;
    private List<Jadwal> jadwal = new ArrayList<>();

    public DokterCardSelection(int id_dokter, String nama, String nama_spesialisasi, String foto_dokter, Date tanggal) {
        this.id_dokter = id_dokter;
        this.nama = nama;
        this.nama_spesialisasi = nama_spesialisasi;
        this.foto_dokter = foto_dokter;
        this.tanggal = tanggal;
    }
}