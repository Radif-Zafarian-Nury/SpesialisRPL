package com.example.spesialisRPL.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
<<<<<<< HEAD
    List<JadwalDokterData> findDoctorsByDay(String day);
    List<String> findSpecializationsByDoctor(String dokter);
    List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi);

=======
>>>>>>> e5683d201a1fac39bb41446ba6aef654764cc5de
    List<JadwalDokterData> findAll();
}
