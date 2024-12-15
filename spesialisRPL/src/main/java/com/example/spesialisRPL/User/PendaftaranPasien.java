package com.example.spesialisRPL.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendaftaranPasien {
    private int id_pendaftaran;
    private int id_pasien;
    private int id_jadwal;
    private boolean status_daftar_ulang;
    private boolean status_bayar;
    private int no_antrian;
}
