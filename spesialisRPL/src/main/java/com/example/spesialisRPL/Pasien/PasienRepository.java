package com.example.spesialisRPL.Pasien;

import java.util.List;

public interface PasienRepository {
    List<Pasien> findAll();
    List<Pasien> searchByMedicalRecordNumber(String noRekamMedis);
}
