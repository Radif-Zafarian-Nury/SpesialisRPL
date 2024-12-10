package com.example.spesialisRPL.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
    List<String> findDoctorsByDay(String day);
    List<String> findSpecializationsByDoctor(String dokter);
    List<String> findSpecializationsByDoctorID(int id);
    List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi);
    List<DokterCard> getAllDoctorCards();
    void registerPasien(String nik, int idJadwal);
    List<JadwalDokterData> findAll();
    JadwalDokterData findScheduleById(int idJadwal);
    void incrementKuotaTerisi(int idJadwal);
    Dokter getDokter(int id);
    List<String> getAllSpesialisasi();
    void updateDokter(Dokter dokter, List<String> spesialisasi);
}
