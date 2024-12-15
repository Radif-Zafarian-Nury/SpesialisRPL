package com.example.spesialisRPL.User;

import java.util.Date;
import java.util.Optional;

public interface UserRepository {
    void saveUser(UserData userData, Date tanggal);
    void saveUserDariAdmin(UserData userData, Date tanggal);
    Optional<UserData> findByNik(String nik);
    Optional<UserData> findByEmail(String email);
}
