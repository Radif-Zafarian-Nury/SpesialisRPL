package com.example.spesialisRPL.Admin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
    List<String> findDoctorsByDay(String day);
    List<String> findSpecializationsByDoctor(String dokter, String tanggal);
    List<String> findSpecializationsByDoctorID(int id);
    List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi, String dokter, String tanggal);
    List<DokterCard> getAllDoctorCards();
    void registerPasien(String nik, int idJadwal, String spesialisasi);
    List<JadwalDokterData> findAll();
    JadwalDokterData findScheduleById(int idJadwal);
    void incrementKuotaTerisi(int idJadwal);
    List<PasienData> findPendaftaranByDate(LocalDate tgl);
    List<PasienData> findPendaftaranByDateAndName(LocalDate tgl, String name);
    List<String> findDoctorNameByPatientName(String name);
    Dokter getDokter(int id);
    List<String> getAllSpesialisasi();
    void updateDokter(Dokter dokter, List<String> spesialisasi, List<JadwalDokterData> jadwal);
    List<JadwalDokterData> findSchedulesByDate(LocalDate tgl);
    List<PasienData> updatePembayaran(int id);
    ArrayList<JadwalDokterData> getFutureJadwalByDoctorID(int id, LocalDate tgl);
    void deleteJadwalById(int idJadwal);
}
