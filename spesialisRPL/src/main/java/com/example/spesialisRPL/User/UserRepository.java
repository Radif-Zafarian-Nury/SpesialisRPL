package com.example.spesialisRPL.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.spesialisRPL.Doctor.Doctor;
import com.example.spesialisRPL.Doctor.DokterCardSelection;

public interface UserRepository {
    void saveUser(UserData userData);
    void saveUserDariAdmin(UserData userData);
    Optional<UserData> findByNik(String nik);
    List<Doctor> findAllDokterMata();
    List<DokterCardSelection> findFilteredDokterMata(LocalDate date);
    List<DokterCardSelection> findFilteredDokterGigi(LocalDate date);
    List<DokterCardSelection> findFilteredDokterTht(LocalDate date);
    List<DokterCardSelection> findFilteredDokterJantung(LocalDate date);
    Optional<UserData> findByEmail(String email);
    Optional<DokterCardSelection> findDokterMataById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> findDokterGigiById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> findDokterThtById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> findDokterJantungById(int id_dokter, Date tanggal);
    int getPatientIdByNik(String nik);
    void daftarPasien(int id_pasien, int id_jadwal, int id_spesialisasi);
    boolean cekPasienTerdaftarJadwal(int id_pasien, int id_jadwal);
    int getIdSpesialisasi(String nama_spesialisasi);
}
