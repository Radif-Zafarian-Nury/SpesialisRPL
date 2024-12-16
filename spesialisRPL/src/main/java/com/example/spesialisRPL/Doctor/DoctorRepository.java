package com.example.spesialisRPL.Doctor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    List<Doctor> getAllDokterMata();
    List<DokterCardSelection> getDoctorMataScheduleByDate(LocalDate date);
    List<DokterCardSelection> getDoctorGigiScheduleByDate(LocalDate date);
    List<DokterCardSelection> getDoctorThtScheduleByDate(LocalDate date);
    List<DokterCardSelection> getDoctorJantungScheduleByDate(LocalDate date);
    Optional<DokterCardSelection> getScheduledDoctorMataById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> getScheduledDoctorGigiById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> getScheduledDoctorThtById(int id_dokter, Date tanggal);
    Optional<DokterCardSelection> getScheduledDoctorJantungById(int id_dokter, Date tanggal);
}