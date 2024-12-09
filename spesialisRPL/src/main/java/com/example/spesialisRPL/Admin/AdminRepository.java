package com.example.spesialisRPL.Admin;

import java.util.List;
import java.util.Optional;

import com.example.spesialisRPL.Dokter.DokterCard;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
    List<String> findDoctorsByDay(String day);
    List<String> findSpecializationsByDoctor(String dokter);
    List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi);
    List<JadwalDokterData> findAll();
    List<DokterCard> getAllDoctorCards();
}
