package com.example.spesialisRPL.Pasien;

import java.time.LocalDate;
import java.util.List;

public interface PasienRepository {
    List<Pasien> findPendaftaranByDate(LocalDate tgl);
    List<Pasien> findPendaftaranByDateAndName(LocalDate tgl, String namaPasien);
}
