package com.example.spesialisRPL.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.spesialisRPL.Doctor.Doctor;
import com.example.spesialisRPL.Doctor.DokterCardSelection;

public interface UserRepository {
    void saveUser(UserData userData, Date tanggal);
    void saveUserDariAdmin(UserData userData, Date tanggal);
    Optional<UserData> findByNik(String nik);
    List<Doctor> findAllDokterMata();
    List<DokterCardSelection> findFilteredDokterMata(LocalDate date);
    Optional<UserData> findByEmail(String email);
    Optional<DokterCardSelection> findDokterMataById(int id_dokter, Date tanggal);
    int getPatientIdByNik(String nik);
    void daftarPasien(int id_pasien, int id_jadwal);
}
