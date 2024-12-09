package com.example.spesialisRPL.RekamMedis;

import lombok.Data;

@Data
public class RekamMedis {
    private int id_pasien;
    private String tanggal;
    private double tinggi_badan;
    private double berat_badan;
    private double suhu_tubuh;
    private String resep_obat;
    private String diagnosa_dokter;
    private String nama;
    private String jenis_kelamin;
    private String tanggal_lahir;
}
