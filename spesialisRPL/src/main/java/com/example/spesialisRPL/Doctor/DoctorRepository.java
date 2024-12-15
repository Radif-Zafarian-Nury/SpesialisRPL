package com.example.spesialisRPL.Doctor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    List<Doctor> getAllDokterMata();
    List<DokterCardSelection> getDoctorScheduleByDate(LocalDate date);
    Optional<DokterCardSelection> getScheduledDoctorById(int id_dokter, Date tanggal);
}