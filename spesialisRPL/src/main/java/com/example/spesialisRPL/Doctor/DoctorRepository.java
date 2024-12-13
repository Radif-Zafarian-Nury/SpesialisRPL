package com.example.spesialisRPL.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface DoctorRepository {
    List<Doctor> getAllDokterMata();
    List<DokterCardSelection> getDoctorScheduleByDate(LocalDate date);
}