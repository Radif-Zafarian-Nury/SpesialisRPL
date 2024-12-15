package com.example.spesialisRPL.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasienData {
    private String nama;
    private String nama_dokter;
    private String nama_spesialisasi;
    private String waktu_mulai;
    private String waktu_selesai;
    private String tanggal;
    private boolean status_bayar;
    private boolean status_daftar_ulang;
    private int no_antrian;
}
