package com.example.spesialisRPL.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
    List<String> findDoctorsByDay(String day);
    List<String> findSpecializationsByDoctor(String dokter);
    List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi);
    void registerPasien(String nik, int idJadwal);
    List<JadwalDokterData> findAll();
}
