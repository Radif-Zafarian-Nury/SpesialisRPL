package com.example.spesialisRPL.User;

import java.util.List;
import java.util.Optional;

import com.example.spesialisRPL.Doctor.Doctor;

public interface UserRepository {
    void saveUser(UserData userData);
    Optional<UserData> findByNik(String nik);
    List<Doctor> findAllDokterMata();
    Optional<UserData> findByEmail(String email);
}
